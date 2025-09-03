import Header from "../../widgets/header/Header";

function Home() {
    return (
        <div className="flex flex-col min-h-screen bg-white dark:bg-gray-950 text-gray-800 dark:text-gray-100">
            {/* Conteúdo principal */}
            <main className="flex-grow pt-20">
                {/* Hero */}
                <section className="w-full px-6 py-16 text-center flex flex-col justify-center">
                    <h1 className="text-5xl font-extrabold text-blue-700 dark:text-blue-400 mb-6">
                        Encontre a vaga ideal para você
                    </h1>
                    <p className="text-lg text-gray-600 dark:text-gray-300 mb-10 max-w-3xl mx-auto">
                        Milhares de oportunidades para candidatos e empresas. Cadastre-se grátis e dê o próximo passo na sua carreira.
                    </p>

                    {/* Barra de busca */}
                    <div className="flex flex-col sm:flex-row items-center justify-center gap-4 max-w-xl mx-auto">
                        <input
                            type="text"
                            placeholder="Busque por cargo ou palavra-chave"
                            className="flex-1 px-4 py-3 rounded-lg border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800 text-gray-700 dark:text-gray-200 focus:outline-none shadow w-full"
                        />
                        <button className="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition shadow w-full sm:w-auto">
                            Buscar
                        </button>
                    </div>
                </section>

                {/* Categorias */}
                <section className="w-full bg-blue-100 dark:bg-gray-900 py-16 transition-colors duration-300">
                    <div className="px-6 text-center w-full">
                        <h2 className="text-3xl font-bold text-blue-700 dark:text-blue-400 mb-10">
                            Explore categorias populares
                        </h2>
                        <div className="grid grid-cols-2 md:grid-cols-4 gap-6 w-full max-w-6xl mx-auto">
                            {["Tecnologia", "Marketing", "Finanças", "Recursos Humanos"].map((categoria) => (
                                <div
                                    key={categoria}
                                    className="p-6 bg-white dark:bg-gray-800 rounded-lg shadow hover:shadow-md cursor-pointer transition text-blue-700 dark:text-blue-300 font-semibold w-full"
                                >
                                    {categoria}
                                </div>
                            ))}
                        </div>
                    </div>
                </section>
            </main>

            {/* Footer */}
            <footer className="w-full bg-blue-600 dark:bg-gray-800 text-white dark:text-gray-300 py-6 text-center">
                <p>© {new Date().getFullYear()} Vagas.com Clone — Todos os direitos reservados</p>
            </footer>
        </div>
    );
}

export default Home;
