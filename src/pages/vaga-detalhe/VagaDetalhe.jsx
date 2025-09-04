import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import Input from "../../components/ui/Input";
import Button from "../../components/ui/Button";

function VagaDetalhe() {
  const { id } = useParams();
  const [vaga, setVaga] = useState(null);
  const [loading, setLoading] = useState(true);
  const [mensagem, setMensagem] = useState("");
  const [erro, setErro] = useState("");

  useEffect(() => {
    async function fetchVaga() {
      try {
        const response = await axios.get(`/api/vagas/${id}`);
        setVaga(response.data);
      } catch (error) {
        console.error("Erro ao buscar vaga:", error);
      } finally {
        setLoading(false);
      }
    }

    fetchVaga();
  }, [id]);

  const handleCandidatura = () => {
    if (!mensagem.trim()) {
      setErro("Por favor, escreva uma mensagem.");
      return;
    }

    alert("Candidatura enviada!");
    setMensagem("");
    setErro("");
  };

  if (loading) return <p>Carregando vaga...</p>;
  if (!vaga) return <p>Vaga não encontrada.</p>;

  return (
    <div className="max-w-3xl mx-auto px-6 py-12 bg-white dark:bg-gray-950 text-gray-800 dark:text-gray-100 rounded-xl shadow">
      <h1 className="text-3xl font-bold text-blue-600 dark:text-blue-400 mb-4">{vaga.titulo}</h1>
      <p><strong>Empresa:</strong> {vaga.empresa}</p>
      <p><strong>Localização:</strong> {vaga.localizacao}</p>
      <p className="mb-6"><strong>Descrição:</strong> {vaga.descricao}</p>

      <div className="space-y-4">
        <Input
          label="Mensagem para a empresa"
          name="mensagem"
          placeholder="Escreva uma breve mensagem..."
          value={mensagem}
          onChange={(e) => setMensagem(e.target.value)}
          error={erro}
        />

        <Button variant="primary" onClick={handleCandidatura}>
          Candidatar-se
        </Button>
      </div>
    </div>
  );
}

export default VagaDetalhe;
