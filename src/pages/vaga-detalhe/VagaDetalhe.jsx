import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import Input from "../../components/ui/Input";
import Button from "../../components/ui/Button";

function VagaDetalhe() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [vaga, setVaga] = useState(null);
  const [loading, setLoading] = useState(true);

  const [mensagem, setMensagem] = useState("");
  const [curriculo, setCurriculo] = useState(null);

  const [erro, setErro] = useState("");
  const [enviando, setEnviando] = useState(false);
  const [feedback, setFeedback] = useState("");

  // Buscar detalhes da vaga
  useEffect(() => {
    async function fetchVaga() {
      try {
        const response = await axios.get(`/api/jobs/${id}`);
        setVaga(response.data);
      } catch (error) {
        console.error("Erro ao buscar vaga:", error);
      } finally {
        setLoading(false);
      }
    }

    fetchVaga();
  }, [id]);

  // Enviar candidatura
  const handleCandidatura = async () => {
    if (!mensagem.trim()) {
      setErro("Por favor, escreva uma mensagem.");
      return;
    }

    if (!curriculo) {
      setErro("Por favor, envie seu currículo em PDF.");
      return;
    }

    setErro("");
    setEnviando(true);
    setFeedback("");

    try {
      const formData = new FormData();
      formData.append("jobId", id);
      formData.append("userId", 123); // 🔧 futuramente pegar do usuário logado
      formData.append("message", mensagem);
      formData.append("resume", curriculo);

      await axios.post("/api/applications", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });

      setFeedback("Candidatura enviada com sucesso!");
      setMensagem("");
      setCurriculo(null);
    } catch (error) {
      console.error("Erro ao enviar candidatura:", error);
      setFeedback("Erro ao enviar candidatura. Tente novamente.");
    } finally {
      setEnviando(false);
    }
  };

  if (loading) return <p>Carregando vaga...</p>;
  if (!vaga) return <p>Vaga não encontrada.</p>;

  return (
    <div className="max-w-3xl mx-auto px-6 py-12 bg-white dark:bg-gray-950 text-gray-800 dark:text-gray-100 rounded-xl shadow">
      <h1 className="text-3xl font-bold text-blue-600 dark:text-blue-400 mb-4">
        {vaga.titulo}
      </h1>
      <p>
        <strong>Empresa:</strong> {vaga.empresa}
      </p>
      <p>
        <strong>Localização:</strong> {vaga.localizacao}
      </p>
      <p className="mb-6">
        <strong>Descrição:</strong> {vaga.descricao}
      </p>

      {vaga.requisitos && (
        <div className="mb-6">
          <h2 className="text-xl font-semibold mb-2">Requisitos</h2>
          <ul className="list-disc pl-6">
            {Array.isArray(vaga.requisitos)
              ? vaga.requisitos.map((req, i) => <li key={i}>{req}</li>)
              : <li>{vaga.requisitos}</li>}
          </ul>
        </div>
      )}

      <div className="space-y-4">
        {/* Mensagem */}
        <Input
          label="Mensagem para a empresa"
          name="mensagem"
          placeholder="Escreva uma breve mensagem..."
          value={mensagem}
          onChange={(e) => setMensagem(e.target.value)}
          error={erro}
        />

        {/* Upload de currículo */}
        <div>
          <label className="block mb-1 font-medium">Currículo (PDF)</label>
          <input
            type="file"
            accept="application/pdf"
            onChange={(e) => setCurriculo(e.target.files[0])}
            className="block w-full border rounded p-2"
          />
        </div>

        {/* Botões */}
        <div className="flex gap-4">
          <Button
            variant="primary"
            onClick={handleCandidatura}
            disabled={enviando}
          >
            {enviando ? "Enviando..." : "Candidatar-se"}
          </Button>

          <Button variant="secondary" onClick={() => navigate("/vagas")}>
            Voltar para vagas
          </Button>
        </div>

        {/* Feedback */}
        {feedback && (
          <p
            className={`mt-2 ${
              feedback.includes("sucesso") ? "text-green-600" : "text-red-600"
            }`}
          >
            {feedback}
          </p>
        )}
      </div>
    </div>
  );
}

export default VagaDetalhe;
