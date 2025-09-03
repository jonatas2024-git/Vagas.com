import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function Login() {
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErro("");

    try {
      const { data } = await axios.post("/api/auth/login", { email, senha });
      localStorage.setItem("token", data.token);
      navigate("/vagas");
    } catch (err) {
      setErro("Credenciais inválidas ou erro de conexão.");
    }
  };

  return (
    <div className="pt-20 min-h-screen bg-white dark:bg-gray-950 flex items-center justify-center">
      <div className="w-full max-w-sm bg-white dark:bg-gray-900 p-8 rounded-2xl shadow-md">
        <h1 className="text-3xl font-bold text-center text-blue-600 dark:text-blue-400 mb-6">
          Login
        </h1>

        {erro && (
          <p className="text-red-500 text-sm text-center mb-4">{erro}</p>
        )}

        <form onSubmit={handleSubmit} className="space-y-5">
          {/* Campo de e-mail */}
          <div>
            <label className="block text-gray-600 dark:text-gray-300 mb-1">
              E-mail
            </label>
            <input
              type="email"
              placeholder="Digite seu e-mail"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full px-4 py-2 border rounded-xl bg-gray-50 dark:bg-gray-800 text-gray-700 dark:text-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          {/* Campo de senha */}
          <div>
            <label className="block text-gray-600 dark:text-gray-300 mb-1">
              Senha
            </label>
            <input
              type="password"
              placeholder="Digite sua senha"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              className="w-full px-4 py-2 border rounded-xl bg-gray-50 dark:bg-gray-800 text-gray-700 dark:text-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          {/* Botão de login */}
          <button
            type="submit"
            className="w-full bg-blue-600 text-white py-2 rounded-xl hover:bg-blue-700 transition duration-200"
          >
            Entrar
          </button>
        </form>

        {/* Link para cadastro */}
        <p className="mt-6 text-sm text-center text-gray-500 dark:text-gray-400">
          Não tem conta?{" "}
          <a
            href="/register"
            className="text-blue-600 dark:text-blue-400 hover:underline font-medium"
          >
            Cadastre-se
          </a>
        </p>
      </div>
    </div>
  );
}

export default Login;
