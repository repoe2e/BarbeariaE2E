import { Link, useLocation } from 'react-router-dom';

export default function Navbar() {
  const { pathname } = useLocation();
  const isAdmin = pathname.startsWith('/admin') && pathname !== '/admin/login';

  return (
    <nav className="navbar">
      <div className="container">
        <Link to="/" className="logo">Barbearia E2E</Link>
        <ul className="nav-links">
          {!isAdmin && (
            <>
              <li><Link to="/" className={pathname === '/' ? 'active' : ''}>Início</Link></li>
              <li><Link to="/agendar" className={pathname === '/agendar' ? 'active' : ''}>Agendar</Link></li>
              <li><Link to="/meus-agendamentos" className={pathname === '/meus-agendamentos' ? 'active' : ''}>Meus Agendamentos</Link></li>
            </>
          )}
          <li>
            {localStorage.getItem('adminToken') ? (
              <Link to="/admin" className={pathname.startsWith('/admin') ? 'active' : ''}>Painel Admin</Link>
            ) : (
              <Link to="/admin/login">Entrar</Link>
            )}
          </li>
        </ul>
      </div>
    </nav>
  );
}
