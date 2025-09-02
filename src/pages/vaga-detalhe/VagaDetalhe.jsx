// src/pages/vaga-detail/VagaDetalhe.jsx

import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";

function VagaDetalhe() {
  const { id } = useParams();
  const [vaga, setVaga] = useState(null);
  const [loading, setLoading] = useState(true);

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

  if (loading) return <p>Carregando vaga...</p>;
  if (!vaga) return <p>Vaga não encontrada.</p>;

  return (
    <div className="vaga-detalhe">
      <h1>{vaga.titulo}</h1>
      <p><strong>Empresa:</strong> {vaga.empresa}</p>
      <p><strong>Localização:</strong> {vaga.localizacao}</p>
      <p><strong>Descrição:</strong> {vaga.descricao}</p>
      <button onClick={() => alert("Candidatura enviada!")}>Candidatar-se</button>
    </div>
  );
}

export default VagaDetalhe;
