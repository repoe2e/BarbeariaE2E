import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { api, formatMoney } from '../services/api';

export default function Home() {
  const [contato, setContato] = useState(null);
  const [servicos, setServicos] = useState([]);
  const [media, setMedia] = useState(0);

  useEffect(() => {
    api.getContato().then(setContato).catch(console.error);
    api.getServicos().then(setServicos).catch(console.error);
    api.getMediaAvaliacoes().then(d => setMedia(d.media)).catch(() => {});
  }, []);

  return (
    <>
      <section className="hero">
        <div className="container hero-content">
          <span className="hero-badge">✂️ Premium desde 2010</span>
          <h1>Seu estilo, <span>nossa arte</span></h1>
          <p>
            Experiência completa de barbearia com profissionais qualificados,
            ambiente moderno e atendimento de excelência.
          </p>
          <div className="hero-actions">
            <Link to="/agendar" className="btn btn-primary">Agendar Agora</Link>
            <Link to="/meus-agendamentos" className="btn btn-outline">Meus Agendamentos</Link>
          </div>
          {media > 0 && (
            <div className="rating-badge">
              <span className="stars">{'★'.repeat(Math.round(media))}{'☆'.repeat(5 - Math.round(media))}</span>
              <span>{media.toFixed(1)} — Avaliação dos clientes</span>
            </div>
          )}
        </div>
      </section>

      <section className="section">
        <div className="container">
          <h2 className="section-title">Nossos Serviços</h2>
          <p className="section-subtitle">Qualidade e precisão em cada detalhe</p>
          <div className="card-grid">
            {servicos.map(s => (
              <div key={s.tipo} className="card">
                <h3>{s.nome}</h3>
                <p>Corte profissional com acabamento impecável.</p>
                <div className="price">{formatMoney(s.preco)}</div>
              </div>
            ))}
          </div>
          <div style={{ textAlign: 'center', marginTop: '2rem' }}>
            <Link to="/agendar" className="btn btn-primary">Reservar Horário</Link>
          </div>
        </div>
      </section>

      {contato && (
        <section className="section" style={{ background: 'var(--bg-card)' }}>
          <div className="container">
            <h2 className="section-title">Contato</h2>
            <p className="section-subtitle">Estamos prontos para atender você</p>
            <div className="contact-grid">
              <div className="contact-item">
                <div className="icon">📍</div>
                <h4>Endereço</h4>
                <p>{contato.endereco}</p>
              </div>
              <div className="contact-item">
                <div className="icon">📞</div>
                <h4>Telefone</h4>
                <p>{contato.telefone}</p>
              </div>
              <div className="contact-item">
                <div className="icon">💬</div>
                <h4>WhatsApp</h4>
                <p>{contato.whatsapp}</p>
              </div>
              <div className="contact-item">
                <div className="icon">✉️</div>
                <h4>E-mail</h4>
                <p>{contato.email}</p>
              </div>
              <div className="contact-item">
                <div className="icon">📸</div>
                <h4>Instagram</h4>
                <p>{contato.instagram}</p>
              </div>
              <div className="contact-item">
                <div className="icon">🕐</div>
                <h4>Horário</h4>
                <p>{contato.horarioFuncionamento}</p>
              </div>
            </div>
          </div>
        </section>
      )}
    </>
  );
}
