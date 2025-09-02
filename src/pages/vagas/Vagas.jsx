// src/pages/vagas/Vagas.jsx

import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";

function Vagas() {
  const [vagas, setVagas] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchVagas() {
      try {
        // Substitua pela sua API real
        const response = await axios.get("/api/vagas");
        setVagas(response.data);
      } catch (error) {
        console.error("Erro ao buscar vagas:", error);
      } finally {
        setLoading(false);
      }
    }

    fetchVagas();
  }, []);

  if (loading) return <p>Carregando vagas...</p>;
  if (vagas.length === 0) return <p>Nenhuma vaga encontrada.</p>;

  return (
    <div className="vagas">
      <h1>Vagas Disponíveis</h1>
      <ul>
        {vagas.map((vaga) => (
          <li key={vaga.id}>
            <h2>{vaga.titulo}</h2>
            <p>{vaga.empresa} — {vaga.localizacao}</p>
            <Link to={`/vaga/${vaga.id}`}>Ver detalhes</Link>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Vagas;
