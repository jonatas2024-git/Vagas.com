// src/pages/vagas/Resultados.jsx

import { useEffect, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import axios from "axios";

function Resultados() {
  const [vagas, setVagas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchParams] = useSearchParams();
  const termo = searchParams.get("q") || ""; // pega ?q= do URL

  useEffect(() => {
    async function fetchVagas() {
      try {
        const response = await axios.get("/api/vagas");
        const data = Array.isArray(response.data)
          ? response.data
          : response.data.vagas || [];

        // filtra já na hora da busca
        const resultados = data.filter(
          (vaga) =>
            vaga.titulo.toLowerCase().includes(termo.toLowerCase()) ||
            vaga.empresa.toLowerCase().includes(termo.toLowerCase()) ||
            vaga.localizacao.toLowerCase().includes(termo.toLowerCase())
        );

        setVagas(resultados);
      } catch (error) {
        console.error("Erro ao buscar vagas:", error);
      } finally {
        setLoading(false);
      }
    }

    fetchVagas();
  }, [termo]);

  if (loading) return <p>Carregando resultados...</p>;

  return (
    <div className="p-6 max-w-3xl mx-auto">
      <h1 className="text-2xl font-bold mb-4">
        Resultados da busca por: "{termo}"
      </h1>

      {vagas.length > 0 ? (
        <ul className="space-y-4">
          {vagas.map((vaga) => (
            <li
              key={vaga.id}
              className="p-4 border rounded-lg shadow-sm hover:shadow-md transition"
            >
              <h2 className="font-semibold text-lg">{vaga.titulo}</h2>
              <p className="text-sm text-gray-600">
                {vaga.empresa} — {vaga.localizacao}
              </p>
              <Link
                to={`/vaga/${vaga.id}`}
                className="text-blue-600 hover:underline"
              >
                Ver detalhes
              </Link>
            </li>
          ))}
        </ul>
      ) : (
        <p className="text-gray-500">Nenhuma vaga encontrada.</p>
      )}
    </div>
  );
}

export default Resultados;
