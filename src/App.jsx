import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

// Páginas
import Home from "./pages/home/Home";
import Login from "./pages/login/login";
import Register from "./pages/register";
import Vagas from "./pages/vagas/Vagas";
import VagaDetalhe from "./pages/vaga-detalhe/VagaDetalhe";

// Header global
import Header from "./widgets/header/Header";

function App() {
  return (
    <Router>
      <div className="min-h-screen flex flex-col bg-gray-50 dark:bg-gray-950">
        {/* Header fixo em todas as páginas */}
        <Header />

        {/* Conteúdo das rotas */}
        <main className="flex-1">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/vagas" element={<Vagas />} />
            <Route path="/vaga/:id" element={<VagaDetalhe />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
