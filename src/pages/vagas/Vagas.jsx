// src/pages/vagas/Vagas.jsx

import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";

function Vagas() {
  const [vagas, setVagas] = useState([]);
  const [loading, setLoading] = useState(true);

  // estados dos filtros
  const [keyword, setKeyword] = useState("");
  const [location, setLocation] = useState("");
  const [contract, setContract] = useState("");

  // Faz a requisição à API já com filtros
  async function fetchVagas(filtros = {}) {
    setLoading(true);
    try {
      const response = await axios.get("/api/vagas", {
        params: filtros, // 🔑 axios já monta ?keyword=...&location=...&contract=...
      });

      const data = Array.isArray(response.data)
        ? response.data
        : response.data.vagas || [];

      setVagas(data);
    } catch (error) {
      console.error("Erro ao buscar vagas:", error);
    } finally {
      setLoading(false);
    }
  }

  // Buscar vagas inicialmente
  useEffect(() => {
    fetchVagas();
  }, []);

  // Submeter filtros
  const handleSubmit = (e) => {
    e.preventDefault();
    fetchVagas({
      keyword,
      location,
      contract,
    });
  };

  if (loading) return <p>Carregando vagas...</p>;

  return (
    <div className="p-6 max-w-4xl mx-auto">
      <h1 className="text-2xl font-bold mb-6">Vagas Disponíveis</h1>

      {/* 🔍 Formulário de filtros */}
      <form
        onSubmit={handleSubmit}
        className="mb-8 grid grid-cols-1 md:grid-cols-3 gap-4"
      >
        <input
          type="text"
          placeholder="Palavra-chave (ex: Frontend)"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          className="p-2 border rounded"
        />

        <input
          type="text"
          placeholder="Localização"
          value={location}
          onChange={(e) => setLocation(e.target.value)}
          className="p-2 border rounded"
        />

        <select
          value={contract}
          onChange={(e) => setContract(e.target.value)}
          className="p-2 border rounded"
        >
          <option value="">Tipo de contrato</option>
          <option value="clt">CLT</option>
          <option value="pj">PJ</option>
          <option value="freelancer">Freelancer</option>
          <option value="estagio">Estágio</option>
        </select>

        <button
          type="submit"
          className="col-span-1 md:col-span-3 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
        >
          Buscar
        </button>
      </form>

      {/* Lista de vagas */}
      {Array.isArray(vagas) && vagas.length > 0 ? (
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
              <p className="text-sm">Contrato: {vaga.tipoContrato}</p>
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

export default Vagas;
