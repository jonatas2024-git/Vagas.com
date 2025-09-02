import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/home/Home";
import Login from "./pages/login/login";
import Register from "./pages/register";
import Vagas from "./pages/vagas/Vagas";
import VagaDetalhe from "./pages/vaga-detalhe/VagaDetalhe";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/vagas" element={<Vagas />} />
        <Route path="/vaga/:id" element={<VagaDetalhe />} />
      </Routes>
    </Router>
  );
}

export default App;
