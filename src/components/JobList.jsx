// src/components/JobList.jsx

import JobCard from "./ui/JobCard";

export default function JobList({ vagas }) {
  return (
    <section className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
      {vagas.map((vaga) => (
        <JobCard
          key={vaga.id}
          titulo={vaga.titulo}
          empresa={vaga.empresa}
          localizacao={vaga.localizacao}
          descricao={vaga.descricao}
          onClick={() => console.log(`Vaga selecionada: ${vaga.id}`)}
        />
      ))}
    </section>
  );
}
    