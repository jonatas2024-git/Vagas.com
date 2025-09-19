import { useState, useEffect } from "react";

export default function JobsPage() {
  const [jobs, setJobs] = useState([]);
  const [search, setSearch] = useState("");
  const [filteredJobs, setFilteredJobs] = useState([]);

  // Buscar as vagas na API
  useEffect(() => {
    async function fetchJobs() {
      try {
        const res = await fetch("/api/jobs");
        const data = await res.json();
        setJobs(data);
        setFilteredJobs(data);
      } catch (error) {
        console.error("Erro ao buscar vagas:", error);
      }
    }
    fetchJobs();
  }, []);

  // Filtrar vagas conforme a busca
  useEffect(() => {
    const lowerSearch = search.toLowerCase();
    setFilteredJobs(
      jobs.filter(
        (job) =>
          job.title.toLowerCase().includes(lowerSearch) ||
          job.company.toLowerCase().includes(lowerSearch)
      )
    );
  }, [search, jobs]);

  return (
    <div className="p-6 max-w-2xl mx-auto">
      <h1 className="text-2xl font-bold mb-4">Vagas Recentes</h1>

      {/* Barra de busca */}
      <input
        type="text"
        placeholder="Buscar vaga..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        className="w-full p-2 border rounded mb-4"
      />

      {/* Lista de vagas */}
      <ul className="space-y-3">
        {filteredJobs.length > 0 ? (
          filteredJobs.map((job) => (
            <li
              key={job.id}
              className="p-4 border rounded-lg shadow-sm hover:shadow-md transition"
            >
              <h2 className="font-semibold">{job.title}</h2>
              <p className="text-sm text-gray-600">{job.company}</p>
              <p className="text-sm">{job.location}</p>
            </li>
          ))
        ) : (
          <p>Nenhuma vaga encontrada.</p>
        )}
      </ul>
    </div>
  );
}
