import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import Home from './pages/Home';
import Agendar from './pages/Agendar';
import MeusAgendamentos from './pages/MeusAgendamentos';
import AdminLogin from './pages/AdminLogin';
import AdminDashboard from './pages/AdminDashboard';

export default function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/agendar" element={<Agendar />} />
        <Route path="/meus-agendamentos" element={<MeusAgendamentos />} />
        <Route path="/admin/login" element={<AdminLogin />} />
        <Route path="/admin" element={<AdminDashboard />} />
      </Routes>
      <Footer />
    </BrowserRouter>
  );
}
