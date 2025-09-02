import { BrowserRouter, Routes, Route } from "react-router-dom";
import HomePage from "@/pages/home";
import LoginPage from "@/pages/login";
import RegisterPage from "@/pages/register";
import VagasPage from "@/pages/vagas";
import VagaDetailPage from "@/pages/vaga-detail";

export const AppRoutes = () => (
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/vagas" element={<VagasPage />} />
      <Route path="/vaga/:id" element={<VagaDetailPage />} />
    </Routes>
  </BrowserRouter>
);
