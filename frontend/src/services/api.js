const API_BASE = '/api';

async function request(url, options = {}) {
  const token = localStorage.getItem('adminToken');
  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...options.headers,
  };

  const res = await fetch(`${API_BASE}${url}`, { ...options, headers });
  const data = await res.json().catch(() => ({}));

  if (!res.ok) {
    throw new Error(data.erro || Object.values(data).join(', ') || 'Erro na requisição');
  }
  return data;
}

export const api = {
  getContato: () => request('/public/contato'),
  getInfo: () => request('/public/info'),
  getServicos: () => request('/agendamentos/servicos'),
  getBarbeiros: () => request('/agendamentos/barbeiros'),
  getHorarios: (barbeiroId, data) => request(`/agendamentos/horarios?barbeiroId=${barbeiroId}&data=${data}`),
  criarAgendamento: (body) => request('/agendamentos', { method: 'POST', body: JSON.stringify(body) }),
  listarAgendamentosCliente: (whatsapp) => request(`/agendamentos/cliente/${encodeURIComponent(whatsapp)}`),
  editarAgendamento: (id, body) => request(`/agendamentos/${id}`, { method: 'PUT', body: JSON.stringify(body) }),
  cancelarAgendamento: (id, motivo) => request(`/agendamentos/${id}`, { method: 'DELETE', body: JSON.stringify({ motivo }) }),
  criarAvaliacao: (body) => request('/agendamentos/avaliacoes', { method: 'POST', body: JSON.stringify(body) }),
  getMediaAvaliacoes: () => request('/agendamentos/avaliacoes/media'),

  login: (usuario, senha) => request('/admin/login', { method: 'POST', body: JSON.stringify({ usuario, senha }) }),
  logout: () => request('/admin/logout', { method: 'POST' }),
  getAgendamentosAdmin: () => request('/admin/agendamentos'),
  concluirAgendamento: (id) => request(`/admin/agendamentos/${id}/concluir`, { method: 'PATCH' }),
  getClientes: () => request('/admin/clientes'),
  getServicosAdmin: () => request('/admin/servicos'),
  atualizarServico: (tipo, body) => request(`/admin/servicos/${tipo}`, { method: 'PUT', body: JSON.stringify(body) }),
  getBarbeirosAdmin: (nome) => request(`/admin/barbeiros${nome ? `?nome=${encodeURIComponent(nome)}` : ''}`),
  criarBarbeiro: (body) => request('/admin/barbeiros', { method: 'POST', body: JSON.stringify(body) }),
  editarBarbeiro: (id, body) => request(`/admin/barbeiros/${id}`, { method: 'PUT', body: JSON.stringify(body) }),
  desativarBarbeiro: (id) => request(`/admin/barbeiros/${id}`, { method: 'DELETE' }),
  getFaturamento: (mes, ano) => request(`/admin/faturamento?mes=${mes}&ano=${ano}`),
  getCancelamentos: () => request('/admin/cancelamentos'),
  getAvaliacoesAdmin: () => request('/admin/avaliacoes'),
  validarAvaliacao: (id) => request(`/admin/avaliacoes/${id}/validar`, { method: 'PATCH' }),
  getRanking: () => request('/admin/ranking'),
};

export const FORMAS_PAGAMENTO = [
  { value: 'DINHEIRO', label: 'Dinheiro' },
  { value: 'PIX', label: 'PIX' },
  { value: 'CARTAO_CREDITO', label: 'Cartão de Crédito' },
  { value: 'CARTAO_DEBITO', label: 'Cartão de Débito' },
];

export const TIPOS_SERVICO = [
  { value: 'CORTE_CABELO', label: 'Corte de Cabelo' },
  { value: 'CORTE_BARBA', label: 'Corte de Barba' },
  { value: 'COMBO', label: 'Combo' },
];

export function formatDateTime(iso) {
  if (!iso) return '';
  const d = new Date(iso);
  return d.toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' });
}

export function formatMoney(value) {
  return value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}
