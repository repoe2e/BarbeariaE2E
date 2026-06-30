import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../services/api';

export default function AdminLogin() {
  const [usuario, setUsuario] = useState('');
  const [senha, setSenha] = useState('');
  const [erro, setErro] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErro('');
    try {
      const res = await api.login(usuario, senha);
      localStorage.setItem('adminToken', res.token);
      navigate('/admin');
    } catch (err) {
      setErro(err.message);
    }
  };

  return (
    <div className="page">
      <div className="container">
        <div className="page-header">
          <h1>Área Administrativa</h1>
          <p style={{ color: 'var(--text-muted)' }}>Acesso restrito ao proprietário</p>
        </div>

        <div className="form-card">
          {erro && <div className="alert alert-error">{erro}</div>}

          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Usuário</label>
              <input value={usuario} onChange={e => setUsuario(e.target.value)} required placeholder="admin" />
            </div>
            <div className="form-group">
              <label>Senha</label>
              <input type="password" value={senha} onChange={e => setSenha(e.target.value)} required placeholder="••••••••" />
            </div>
            <button type="submit" className="btn btn-primary" style={{ width: '100%' }}>Entrar</button>
          </form>

          <p style={{ marginTop: '1.5rem', textAlign: 'center', color: 'var(--text-muted)', fontSize: '0.85rem' }}>
            Credenciais de teste: <strong style={{ color: 'var(--gold)' }}>admin</strong> / <strong style={{ color: 'var(--gold)' }}>admin123</strong>
          </p>
        </div>
      </div>
    </div>
  );
}
