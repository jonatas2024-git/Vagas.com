// src/pages/perfil/Perfil.jsx
import { useEffect, useState } from "react";
import axios from "axios";

function Perfil() {
  const userId = 123; // 🔧 futuramente pegar do AuthProvider
  const [user, setUser] = useState(null);
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchData() {
      try {
        // Dados do usuário
        const userRes = await axios.get(`/api/users/${userId}`);
        setUser(userRes.data);

        // Candidaturas
        const appsRes = await axios.get(`/api/applications/user/${userId}`);
        const appsData = Array.isArray(appsRes.data)
          ? appsRes.data
          : appsRes.data.candidaturas || appsRes.data.applications || [];
        setApplications(appsData);
      } catch (error) {
        console.error("Erro ao carregar perfil:", error);
      } finally {
        setLoading(false);
      }
    }

    fetchData();
  }, [userId]);

  if (loading) return <p>Carregando perfil...</p>;
  if (!user) return <p>Usuário não encontrado.</p>;

  return (
    <div className="max-w-4xl mx-auto px-6 py-12 bg-white dark:bg-gray-950 text-gray-800 dark:text-gray-100 rounded-xl shadow">
      {/* Dados do usuário */}
      <h1 className="text-3xl font-bold text-blue-600 dark:text-blue-400 mb-4">
        Perfil do Usuário
      </h1>
      <p>
        <strong>Nome:</strong> {user?.nome || user?.name || "Não informado"}
      </p>
      <p>
        <strong>Email:</strong> {user?.email || "Não informado"}
      </p>

      {/* Histórico */}
      <div className="mt-8">
        <h2 className="text-2xl font-semibold mb-4">Minhas Candidaturas</h2>

        {Array.isArray(applications) && applications.length > 0 ? (
          <ul className="space-y-4">
            {applications.map((app) => (
              <li
                key={app.id}
                className="p-4 border rounded-lg shadow-sm hover:shadow-md transition"
              >
                <h3 className="font-semibold text-lg">
                  {app?.vaga?.titulo || "Vaga não disponível"}
                </h3>
                <p className="text-sm text-gray-600">
                  {(app?.vaga?.empresa || "Empresa não informada")} —{" "}
                  {(app?.vaga?.localizacao || "Localização não informada")}
                </p>
                <p className="text-sm">
                  Status:{" "}
                  <span className="font-medium">{app?.status || "Pendente"}</span>
                </p>
                <p className="text-xs text-gray-500">
                  Data:{" "}
                  {app?.createdAt
                    ? new Date(app.createdAt).toLocaleDateString()
                    : "—"}
                </p>
              </li>
            ))}
          </ul>
        ) : (
          <p className="text-gray-500">
            Você ainda não se candidatou a nenhuma vaga.
          </p>
        )}
      </div>
    </div>
  );
}

export default Perfil;
