import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";
import { useAuth } from "../../app/providers/AuthProvider";
import Input from "../../components/ui/Input";
import Button from "../../components/ui/Button";

function Login() {
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErro("");
    setLoading(true);

    try {
      const { data } = await axios.post("/api/auth/login", { email, senha });

      login(data.token, data.user);
      navigate("/vagas");
    } catch (err) {
      setErro("Credenciais inválidas ou erro de conexão.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-4 mt-[-4rem]">
      <div className="w-full max-w-sm bg-white dark:bg-gray-900 p-8 rounded-2xl shadow-md">
        <h1 className="text-3xl font-bold text-center text-blue-600 dark:text-blue-400 mb-6">
          Login
        </h1>

        {erro && (
          <p className="text-red-500 text-sm text-center mb-4">{erro}</p>
        )}

        <form onSubmit={handleSubmit} className="space-y-5">
          <Input
            label="E-mail"
            name="email"
            type="email"
            placeholder="Digite seu e-mail"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            error={erro && !senha ? erro : ""}
          />

          <Input
            label="Senha"
            name="senha"
            type="password"
            placeholder="Digite sua senha"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            error={erro && !email ? erro : ""}
          />

          <Button
            type="submit"
            variant="primary"
            disabled={loading}
            className="w-full"
          >
            {loading ? "Entrando..." : "Entrar"}
          </Button>
        </form>

        <p className="mt-6 text-sm text-center text-gray-500 dark:text-gray-400">
          Não tem conta?{" "}
          <Link
            to="/register"
            className="text-blue-600 dark:text-blue-400 hover:underline font-medium"
          >
            Cadastre-se
          </Link>
        </p>
      </div>
    </div>
  );
}

export default Login;
