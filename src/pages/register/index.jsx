import { useState } from "react";
import axios from "axios";
import { useNavigate, Link } from "react-router-dom";
import Input from "../../components/ui/Input";
import Button from "../../components/ui/Button";

function Register() {
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [tipo, setTipo] = useState("candidato");
  const [erro, setErro] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErro("");

    try {
      const { data } = await axios.post("/api/auth/register", {
        nome,
        email,
        senha,
        tipo,
      });

      if (data.token) {
        localStorage.setItem("token", data.token);
      }

      navigate("/vagas");
    } catch (err) {
      setErro("Erro ao cadastrar. Verifique os dados e tente novamente.");
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-950 flex items-center justify-center -translate-y-6">
      <div className="w-full max-w-md bg-white dark:bg-gray-900 p-8 rounded-2xl shadow-md">
        <h1 className="text-3xl font-bold text-center text-blue-600 dark:text-blue-400 mb-6">
          Cadastro
        </h1>

        {erro && <p className="text-red-500 text-sm text-center mb-4">{erro}</p>}

        <form onSubmit={handleSubmit} className="space-y-5">
          <Input
            label="Nome"
            name="nome"
            type="text"
            placeholder="Seu nome"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            error={erro && !nome ? erro : ""}
            required
          />

          <Input
            label="E-mail"
            name="email"
            type="email"
            placeholder="Seu e-mail"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            error={erro && !email ? erro : ""}
            required
          />

          <Input
            label="Senha"
            name="senha"
            type="password"
            placeholder="Crie uma senha"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            error={erro && !senha ? erro : ""}
            required
          />

          <div>
            <label className="block text-gray-600 dark:text-gray-300 mb-1">
              Tipo de conta
            </label>
            <select
              value={tipo}
              onChange={(e) => setTipo(e.target.value)}
              className="w-full px-4 py-2 border rounded-xl bg-gray-50 dark:bg-gray-800 text-gray-700 dark:text-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="candidato">Candidato</option>
              <option value="empresa">Empresa</option>
            </select>
          </div>

          <Button type="submit" variant="primary" className="w-full">
            Cadastrar
          </Button>
        </form>

        <p className="mt-6 text-sm text-center text-gray-500 dark:text-gray-400">
          Já tem conta?{" "}
          <Link
            to="/login"
            className="text-blue-600 dark:text-blue-400 hover:underline font-medium"
          >
            Faça login
          </Link>
        </p>
      </div>
    </div>
  );
}

export default Register;
