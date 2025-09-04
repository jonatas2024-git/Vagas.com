// src/components/ui/JobCard.jsx

export default function JobCard({ titulo, empresa, localizacao, descricao, onClick }) {
  return (
    <div
      className="bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-700 rounded-xl p-6 shadow hover:shadow-md transition cursor-pointer"
      onClick={onClick}
    >
      <h3 className="text-xl font-bold text-blue-700 dark:text-blue-400 mb-2">{titulo}</h3>
      <p className="text-sm text-gray-700 dark:text-gray-300 mb-1">
        <strong>Empresa:</strong> {empresa}
      </p>
      <p className="text-sm text-gray-700 dark:text-gray-300 mb-1">
        <strong>Localização:</strong> {localizacao}
      </p>
      <p className="text-sm text-gray-600 dark:text-gray-400 line-clamp-3">
        {descricao}
      </p>
    </div>
  );
}
