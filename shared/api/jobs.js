export async function getJobs() {
  const res = await fetch("/api/jobs");
  if (!res.ok) throw new Error("Erro ao buscar vagas");
  return res.json();
}
