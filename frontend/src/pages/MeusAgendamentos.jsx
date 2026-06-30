import { useState } from 'react';
import { api, formatDateTime, formatMoney, FORMAS_PAGAMENTO, TIPOS_SERVICO } from '../services/api';

export default function MeusAgendamentos() {
  const [whatsapp, setWhatsapp] = useState(localStorage.getItem('clientWhatsapp') || '');
  const [agendamentos, setAgendamentos] = useState([]);
  const [erro, setErro] = useState('');
  const [editando, setEditando] = useState(null);
  const [avaliando, setAvaliando] = useState(null);
  const [nota, setNota] = useState(5);
  const [comentario, setComentario] = useState('');

  const buscar = async () => {
    setErro('');
    try {
      localStorage.setItem('clientWhatsapp', whatsapp);
      const data = await api.listarAgendamentosCliente(whatsapp);
      setAgendamentos(data);
      if (data.length === 0) setErro('Nenhum agendamento encontrado para este WhatsApp.');
    } catch (err) {
      setErro(err.message);
    }
  };

  const cancelar = async (id) => {
    if (!confirm('Deseja cancelar este agendamento?')) return;
    try {
      await api.cancelarAgendamento(id, 'Cancelado pelo cliente');
      buscar();
    } catch (err) {
      alert(err.message);
    }
  };

  const salvarEdicao = async (e) => {
    e.preventDefault();
    const fd = new FormData(e.target);
    try {
      await api.editarAgendamento(editando.id, {
        tipoServico: fd.get('tipoServico') || undefined,
        formaPagamento: fd.get('formaPagamento') || undefined,
        barbeiroId: fd.get('barbeiroId') || undefined,
        data: fd.get('data'),
        hora: fd.get('hora'),
      });
      setEditando(null);
      buscar();
    } catch (err) {
      alert(err.message);
    }
  };

  const enviarAvaliacao = async (e) => {
    e.preventDefault();
    try {
      await api.criarAvaliacao({ agendamentoId: avaliando.id, nota, comentario });
      setAvaliando(null);
      alert('Avaliação enviada! Obrigado.');
    } catch (err) {
      alert(err.message);
    }
  };

  const statusLabel = { AGENDADO: 'Agendado', CONCLUIDO: 'Concluído', CANCELADO: 'Cancelado' };

  return (
    <div className="page">
      <div className="container">
        <div className="page-header">
          <h1>Meus Agendamentos</h1>
          <p style={{ color: 'var(--text-muted)' }}>Consulte, edite ou cancele suas reservas</p>
        </div>

        <div className="form-card" style={{ marginBottom: '2rem' }}>
          <div className="form-group">
            <label>Seu WhatsApp</label>
            <div style={{ display: 'flex', gap: '0.5rem' }}>
              <input value={whatsapp} onChange={e => setWhatsapp(e.target.value)} placeholder="(11) 99999-9999" style={{ flex: 1 }} />
              <button className="btn btn-primary" onClick={buscar}>Buscar</button>
            </div>
          </div>
          {erro && <div className="alert alert-error">{erro}</div>}
        </div>

        {agendamentos.length > 0 && (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Serviço</th>
                  <th>Barbeiro</th>
                  <th>Data/Hora</th>
                  <th>Valor</th>
                  <th>Status</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {agendamentos.map(ag => (
                  <tr key={ag.id}>
                    <td>{ag.servicoNome}</td>
                    <td>{ag.barbeiroNome}</td>
                    <td>{formatDateTime(ag.dataHora)}</td>
                    <td>{formatMoney(ag.valor)}</td>
                    <td><span className={`status status-${ag.status.toLowerCase()}`}>{statusLabel[ag.status]}</span></td>
                    <td style={{ display: 'flex', gap: '0.4rem', flexWrap: 'wrap' }}>
                      {ag.podeEditar && (
                        <>
                          <button className="btn btn-outline btn-sm" onClick={() => setEditando(ag)}>Editar</button>
                          <button className="btn btn-danger btn-sm" onClick={() => cancelar(ag.id)}>Cancelar</button>
                        </>
                      )}
                      {ag.status === 'CONCLUIDO' && (
                        <button className="btn btn-primary btn-sm" onClick={() => setAvaliando(ag)}>Avaliar</button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {editando && (
          <div className="modal-overlay" onClick={() => setEditando(null)}>
            <div className="modal" onClick={e => e.stopPropagation()}>
              <h2>Editar Agendamento</h2>
              <form onSubmit={salvarEdicao}>
                <div className="form-group">
                  <label>Serviço</label>
                  <select name="tipoServico" defaultValue={editando.tipoServico}>
                    {TIPOS_SERVICO.map(t => <option key={t.value} value={t.value}>{t.label}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label>Pagamento</label>
                  <select name="formaPagamento" defaultValue={editando.formaPagamento}>
                    {FORMAS_PAGAMENTO.map(f => <option key={f.value} value={f.value}>{f.label}</option>)}
                  </select>
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label>Data</label>
                    <input name="data" type="date" defaultValue={editando.dataHora?.split('T')[0]} required />
                  </div>
                  <div className="form-group">
                    <label>Hora</label>
                    <input name="hora" defaultValue={editando.dataHora?.split('T')[1]?.slice(0, 5)} required />
                  </div>
                </div>
                <div style={{ display: 'flex', gap: '0.5rem', marginTop: '1rem' }}>
                  <button type="submit" className="btn btn-primary">Salvar</button>
                  <button type="button" className="btn btn-outline" onClick={() => setEditando(null)}>Fechar</button>
                </div>
              </form>
            </div>
          </div>
        )}

        {avaliando && (
          <div className="modal-overlay" onClick={() => setAvaliando(null)}>
            <div className="modal" onClick={e => e.stopPropagation()}>
              <h2>Avaliar Atendimento</h2>
              <form onSubmit={enviarAvaliacao}>
                <div className="form-group">
                  <label>Nota (1 a 5)</label>
                  <select value={nota} onChange={e => setNota(parseInt(e.target.value))}>
                    {[5,4,3,2,1].map(n => <option key={n} value={n}>{n} estrela{n > 1 ? 's' : ''}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label>Comentário (opcional)</label>
                  <textarea value={comentario} onChange={e => setComentario(e.target.value)} rows={3} />
                </div>
                <button type="submit" className="btn btn-primary">Enviar Avaliação</button>
              </form>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
