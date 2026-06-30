import { useEffect, useState } from 'react';
import { api, FORMAS_PAGAMENTO, TIPOS_SERVICO } from '../services/api';

export default function Agendar() {
  const [barbeiros, setBarbeiros] = useState([]);
  const [servicos, setServicos] = useState([]);
  const [horarios, setHorarios] = useState([]);
  const [form, setForm] = useState({
    nome: '', whatsapp: '', diaNascimento: '', mesNascimento: '',
    tipoServico: 'CORTE_CABELO', formaPagamento: 'PIX',
    barbeiroId: '', data: '', hora: '',
  });
  const [erro, setErro] = useState('');
  const [sucesso, setSucesso] = useState('');

  useEffect(() => {
    api.getBarbeiros().then(setBarbeiros).catch(console.error);
    api.getServicos().then(setServicos).catch(console.error);
  }, []);

  useEffect(() => {
    if (form.barbeiroId && form.data) {
      api.getHorarios(form.barbeiroId, form.data)
        .then(setHorarios)
        .catch(() => setHorarios([]));
    } else {
      setHorarios([]);
    }
  }, [form.barbeiroId, form.data]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErro('');
    setSucesso('');
    try {
      const res = await api.criarAgendamento({
        ...form,
        diaNascimento: parseInt(form.diaNascimento),
        mesNascimento: parseInt(form.mesNascimento),
      });
      setSucesso(`Agendamento confirmado! ID: ${res.id.slice(0, 8)}... — ${res.servicoNome} em ${new Date(res.dataHora).toLocaleString('pt-BR')}`);
      setForm({ ...form, data: '', hora: '' });
    } catch (err) {
      setErro(err.message);
    }
  };

  const precoSelecionado = servicos.find(s => s.tipo === form.tipoServico)?.preco;
  const minDate = new Date().toISOString().split('T')[0];

  return (
    <div className="page">
      <div className="container">
        <div className="page-header">
          <h1>Agendar Serviço</h1>
          <p style={{ color: 'var(--text-muted)' }}>Horários disponíveis das 09:00 às 20:00</p>
        </div>

        <div className="form-card">
          {erro && <div className="alert alert-error">{erro}</div>}
          {sucesso && <div className="alert alert-success">{sucesso}</div>}

          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Nome completo</label>
              <input name="nome" value={form.nome} onChange={handleChange} required placeholder="Seu nome" />
            </div>

            <div className="form-group">
              <label>WhatsApp</label>
              <input name="whatsapp" value={form.whatsapp} onChange={handleChange} required placeholder="(11) 99999-9999" />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Dia de nascimento</label>
                <input name="diaNascimento" type="number" min="1" max="31" value={form.diaNascimento} onChange={handleChange} required />
              </div>
              <div className="form-group">
                <label>Mês de nascimento</label>
                <input name="mesNascimento" type="number" min="1" max="12" value={form.mesNascimento} onChange={handleChange} required />
              </div>
            </div>

            <div className="form-group">
              <label>Serviço</label>
              <select name="tipoServico" value={form.tipoServico} onChange={handleChange}>
                {TIPOS_SERVICO.map(t => (
                  <option key={t.value} value={t.value}>{t.label}</option>
                ))}
              </select>
              {precoSelecionado && <p style={{ marginTop: '0.5rem', color: 'var(--gold)' }}>Valor: R$ {precoSelecionado.toFixed(2)}</p>}
            </div>

            <div className="form-group">
              <label>Forma de pagamento</label>
              <select name="formaPagamento" value={form.formaPagamento} onChange={handleChange}>
                {FORMAS_PAGAMENTO.map(f => (
                  <option key={f.value} value={f.value}>{f.label}</option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label>Barbeiro</label>
              <select name="barbeiroId" value={form.barbeiroId} onChange={handleChange} required>
                <option value="">Selecione um barbeiro</option>
                {barbeiros.map(b => (
                  <option key={b.id} value={b.id}>{b.nome} — {b.especialidade}</option>
                ))}
              </select>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Data</label>
                <input name="data" type="date" min={minDate} value={form.data} onChange={handleChange} required />
              </div>
              <div className="form-group">
                <label>Horário</label>
                <select name="hora" value={form.hora} onChange={handleChange} required>
                  <option value="">Selecione</option>
                  {horarios.map(h => (
                    <option key={h} value={h}>{h}</option>
                  ))}
                </select>
              </div>
            </div>

            <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '0.5rem' }}>
              Confirmar Agendamento
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
