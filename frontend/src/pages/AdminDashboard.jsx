import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api, formatDateTime, formatMoney } from '../services/api';

const TABS = [
  { id: 'agendamentos', label: '📅 Agendamentos' },
  { id: 'clientes', label: '👥 Clientes' },
  { id: 'servicos', label: '✂️ Serviços' },
  { id: 'barbeiros', label: '💈 Barbeiros' },
  { id: 'faturamento', label: '💰 Faturamento' },
  { id: 'cancelamentos', label: '❌ Cancelamentos' },
  { id: 'avaliacoes', label: '⭐ Avaliações' },
  { id: 'ranking', label: '🏆 Ranking' },
];

export default function AdminDashboard() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('agendamentos');
  const [data, setData] = useState({});
  const [loading, setLoading] = useState(false);
  const [mes, setMes] = useState(new Date().getMonth() + 1);
  const [ano, setAno] = useState(new Date().getFullYear());
  const [buscaBarbeiro, setBuscaBarbeiro] = useState('');
  const [novoBarbeiro, setNovoBarbeiro] = useState({ nome: '', especialidade: '' });

  useEffect(() => {
    if (!localStorage.getItem('adminToken')) {
      navigate('/admin/login');
    }
  }, [navigate]);

  const load = async (currentTab) => {
    setLoading(true);
    try {
      let result;
      switch (currentTab) {
        case 'agendamentos':
          result = { agendamentos: await api.getAgendamentosAdmin() };
          break;
        case 'clientes':
          result = { clientes: await api.getClientes() };
          break;
        case 'servicos':
          result = { servicos: await api.getServicosAdmin() };
          break;
        case 'barbeiros':
          result = { barbeiros: await api.getBarbeirosAdmin(buscaBarbeiro) };
          break;
        case 'faturamento':
          result = { faturamento: await api.getFaturamento(mes, ano) };
          break;
        case 'cancelamentos':
          result = { cancelamentos: await api.getCancelamentos() };
          break;
        case 'avaliacoes':
          result = { avaliacoes: await api.getAvaliacoesAdmin() };
          break;
        case 'ranking':
          result = { ranking: await api.getRanking() };
          break;
        default:
          result = {};
      }
      setData(d => ({ ...d, ...result }));
    } catch {
      localStorage.removeItem('adminToken');
      navigate('/admin/login');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(tab); }, [tab, mes, ano]);

  const logout = async () => {
    try { await api.logout(); } catch {}
    localStorage.removeItem('adminToken');
    navigate('/admin/login');
  };

  const concluir = async (id) => {
    await api.concluirAgendamento(id);
    load('agendamentos');
  };

  const atualizarServico = async (tipo, preco, nome, ativo) => {
    await api.atualizarServico(tipo, { preco: parseFloat(preco), nome, ativo });
    load('servicos');
  };

  const criarBarbeiro = async (e) => {
    e.preventDefault();
    await api.criarBarbeiro(novoBarbeiro);
    setNovoBarbeiro({ nome: '', especialidade: '' });
    load('barbeiros');
  };

  const editarBarbeiro = async (id, nome, especialidade) => {
    await api.editarBarbeiro(id, { nome, especialidade, ativo: true });
    load('barbeiros');
  };

  const desativarBarbeiro = async (id) => {
    if (!confirm('Desativar este barbeiro?')) return;
    await api.desativarBarbeiro(id);
    load('barbeiros');
  };

  const validarAvaliacao = async (id) => {
    await api.validarAvaliacao(id);
    load('avaliacoes');
  };

  const statusLabel = { AGENDADO: 'Agendado', CONCLUIDO: 'Concluído', CANCELADO: 'Cancelado' };

  return (
    <div className="admin-layout">
      <aside className="admin-sidebar">
        {TABS.map(t => (
          <button key={t.id} className={tab === t.id ? 'active' : ''} onClick={() => setTab(t.id)}>
            {t.label}
          </button>
        ))}
        <button onClick={logout} style={{ marginTop: '1rem', color: 'var(--danger)' }}>🚪 Sair</button>
      </aside>

      <main className="admin-content">
        <h1 style={{ marginBottom: '1.5rem', fontSize: '1.8rem' }}>
          {TABS.find(t => t.id === tab)?.label.replace(/^[^\s]+\s/, '')}
        </h1>

        {loading && <p style={{ color: 'var(--text-muted)' }}>Carregando...</p>}

        {tab === 'agendamentos' && data.agendamentos && (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Cliente</th>
                  <th>Barbeiro</th>
                  <th>Serviço</th>
                  <th>Data/Hora</th>
                  <th>Valor</th>
                  <th>Status</th>
                  <th>Ação</th>
                </tr>
              </thead>
              <tbody>
                {data.agendamentos.map(ag => (
                  <tr key={ag.id}>
                    <td>{ag.clienteNome}<br /><small style={{ color: 'var(--text-muted)' }}>{ag.clienteWhatsapp}</small></td>
                    <td>{ag.barbeiroNome}</td>
                    <td>{ag.servicoNome}</td>
                    <td>{formatDateTime(ag.dataHora)}</td>
                    <td>{formatMoney(ag.valor)}</td>
                    <td><span className={`status status-${ag.status.toLowerCase()}`}>{statusLabel[ag.status]}</span></td>
                    <td>
                      {ag.status === 'AGENDADO' && (
                        <button className="btn btn-primary btn-sm" onClick={() => concluir(ag.id)}>Concluir</button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {tab === 'clientes' && data.clientes && (
          <div className="table-wrap">
            <table>
              <thead>
                <tr><th>Nome</th><th>WhatsApp</th><th>Nascimento</th></tr>
              </thead>
              <tbody>
                {data.clientes.map(c => (
                  <tr key={c.id}>
                    <td>{c.nome}</td>
                    <td>{c.whatsapp}</td>
                    <td>{String(c.diaNascimento).padStart(2,'0')}/{String(c.mesNascimento).padStart(2,'0')}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {tab === 'servicos' && data.servicos && (
          <div className="card-grid">
            {data.servicos.map(s => (
              <ServicoCard key={s.tipo} servico={s} onSave={atualizarServico} />
            ))}
          </div>
        )}

        {tab === 'barbeiros' && (
          <>
            <div style={{ display: 'flex', gap: '0.5rem', marginBottom: '1.5rem' }}>
              <input
                placeholder="Buscar barbeiro..."
                value={buscaBarbeiro}
                onChange={e => setBuscaBarbeiro(e.target.value)}
                style={{ flex: 1, padding: '0.75rem', background: 'var(--bg-elevated)', border: '1px solid var(--border)', borderRadius: '8px', color: 'var(--text)' }}
              />
              <button className="btn btn-outline" onClick={() => load('barbeiros')}>Buscar</button>
            </div>

            <form onSubmit={criarBarbeiro} className="form-card" style={{ maxWidth: '100%', marginBottom: '1.5rem' }}>
              <h3 style={{ marginBottom: '1rem', color: 'var(--gold)' }}>Novo Barbeiro</h3>
              <div className="form-row">
                <div className="form-group">
                  <label>Nome</label>
                  <input value={novoBarbeiro.nome} onChange={e => setNovoBarbeiro({ ...novoBarbeiro, nome: e.target.value })} required />
                </div>
                <div className="form-group">
                  <label>Especialidade</label>
                  <input value={novoBarbeiro.especialidade} onChange={e => setNovoBarbeiro({ ...novoBarbeiro, especialidade: e.target.value })} required />
                </div>
              </div>
              <button type="submit" className="btn btn-primary">Adicionar</button>
            </form>

            {data.barbeiros && (
              <div className="table-wrap">
                <table>
                  <thead>
                    <tr><th>Nome</th><th>Especialidade</th><th>Status</th><th>Ações</th></tr>
                  </thead>
                  <tbody>
                    {data.barbeiros.map(b => (
                      <BarbeiroRow key={b.id} barbeiro={b} onEdit={editarBarbeiro} onDesativar={desativarBarbeiro} />
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </>
        )}

        {tab === 'faturamento' && (
          <>
            <div className="form-row" style={{ maxWidth: '400px', marginBottom: '1.5rem' }}>
              <div className="form-group">
                <label>Mês</label>
                <input type="number" min="1" max="12" value={mes} onChange={e => setMes(parseInt(e.target.value))} />
              </div>
              <div className="form-group">
                <label>Ano</label>
                <input type="number" value={ano} onChange={e => setAno(parseInt(e.target.value))} />
              </div>
            </div>
            {data.faturamento && (
              <div className="stats-grid">
                <div className="stat-card">
                  <h4>Faturamento Realizado</h4>
                  <div className="value">{formatMoney(data.faturamento.realizado)}</div>
                  <small style={{ color: 'var(--text-muted)' }}>Serviços concluídos</small>
                </div>
                <div className="stat-card">
                  <h4>Faturamento Previsto</h4>
                  <div className="value">{formatMoney(data.faturamento.previsto)}</div>
                  <small style={{ color: 'var(--text-muted)' }}>Agendados + concluídos</small>
                </div>
              </div>
            )}
          </>
        )}

        {tab === 'cancelamentos' && data.cancelamentos && (
          <div className="table-wrap">
            <table>
              <thead>
                <tr><th>Data Agendamento</th><th>Valor</th><th>Motivo</th><th>Cancelado em</th></tr>
              </thead>
              <tbody>
                {data.cancelamentos.map(c => (
                  <tr key={c.id}>
                    <td>{formatDateTime(c.dataHoraAgendamento)}</td>
                    <td>{formatMoney(c.valor)}</td>
                    <td>{c.motivo}</td>
                    <td>{formatDateTime(c.canceladoEm)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {tab === 'avaliacoes' && data.avaliacoes && (
          <div className="table-wrap">
            <table>
              <thead>
                <tr><th>Nota</th><th>Comentário</th><th>Validada</th><th>Data</th><th>Ação</th></tr>
              </thead>
              <tbody>
                {data.avaliacoes.map(a => (
                  <tr key={a.id}>
                    <td>{'★'.repeat(a.nota)}</td>
                    <td>{a.comentario || '—'}</td>
                    <td>{a.validada ? '✅ Sim' : '⏳ Pendente'}</td>
                    <td>{formatDateTime(a.criadaEm)}</td>
                    <td>
                      {!a.validada && (
                        <button className="btn btn-primary btn-sm" onClick={() => validarAvaliacao(a.id)}>Validar</button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {tab === 'ranking' && data.ranking && (
          <div className="card-grid">
            {data.ranking.map((r, i) => (
              <div key={r.barbeiroId} className="card">
                <h3>#{i + 1} {r.barbeiroNome}</h3>
                <p>Média: <strong style={{ color: 'var(--gold)' }}>{r.mediaNota || '—'}</strong> ({r.totalAvaliacoes} avaliações)</p>
                <p>Atendimentos concluídos: {r.totalAtendimentos}</p>
              </div>
            ))}
          </div>
        )}
      </main>
    </div>
  );
}

function ServicoCard({ servico, onSave }) {
  const [preco, setPreco] = useState(servico.preco);
  const [nome, setNome] = useState(servico.nome);
  const [ativo, setAtivo] = useState(servico.ativo);

  return (
    <div className="card">
      <h3>{servico.tipo.replace(/_/g, ' ')}</h3>
      <div className="form-group">
        <label>Nome</label>
        <input value={nome} onChange={e => setNome(e.target.value)} />
      </div>
      <div className="form-group">
        <label>Preço (R$)</label>
        <input type="number" step="0.01" value={preco} onChange={e => setPreco(e.target.value)} />
      </div>
      <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '1rem' }}>
        <input type="checkbox" checked={ativo} onChange={e => setAtivo(e.target.checked)} />
        Ativo
      </label>
      <button className="btn btn-primary btn-sm" onClick={() => onSave(servico.tipo, preco, nome, ativo)}>Salvar</button>
    </div>
  );
}

function BarbeiroRow({ barbeiro, onEdit, onDesativar }) {
  const [editando, setEditando] = useState(false);
  const [nome, setNome] = useState(barbeiro.nome);
  const [esp, setEsp] = useState(barbeiro.especialidade);

  if (editando) {
    return (
      <tr>
        <td><input value={nome} onChange={e => setNome(e.target.value)} style={{ width: '100%' }} /></td>
        <td><input value={esp} onChange={e => setEsp(e.target.value)} style={{ width: '100%' }} /></td>
        <td>{barbeiro.ativo ? 'Ativo' : 'Inativo'}</td>
        <td>
          <button className="btn btn-primary btn-sm" onClick={() => { onEdit(barbeiro.id, nome, esp); setEditando(false); }}>Salvar</button>
          <button className="btn btn-outline btn-sm" onClick={() => setEditando(false)}>Cancelar</button>
        </td>
      </tr>
    );
  }

  return (
    <tr>
      <td>{barbeiro.nome}</td>
      <td>{barbeiro.especialidade}</td>
      <td>{barbeiro.ativo ? '✅ Ativo' : '❌ Inativo'}</td>
      <td style={{ display: 'flex', gap: '0.4rem' }}>
        <button className="btn btn-outline btn-sm" onClick={() => setEditando(true)}>Editar</button>
        {barbeiro.ativo && <button className="btn btn-danger btn-sm" onClick={() => onDesativar(barbeiro.id)}>Desativar</button>}
      </td>
    </tr>
  );
}
