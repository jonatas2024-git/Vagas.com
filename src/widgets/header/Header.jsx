import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { Sun, Moon, Menu } from "lucide-react";

function Header() {
  const [darkMode, setDarkMode] = useState(false);
  const [menuOpen, setMenuOpen] = useState(false);

  useEffect(() => {
    if (localStorage.getItem("theme") === "dark") {
      document.documentElement.classList.add("dark");
      setDarkMode(true);
    }
  }, []);

  const toggleDarkMode = () => {
    setDarkMode(!darkMode);
    if (!darkMode) {
      document.documentElement.classList.add("dark");
      localStorage.setItem("theme", "dark");
    } else {
      document.documentElement.classList.remove("dark");
      localStorage.setItem("theme", "light");
    }
  };

  return (
    <header className="bg-white dark:bg-gray-900 shadow-sm border-b border-gray-300 dark:border-gray-700 transition-colors duration-300">
      <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
        {/* Logo */}
        <Link to="/" className="text-2xl font-bold text-blue-600 dark:text-blue-400">
          Vagas.com
        </Link>

        {/* Desktop Menu */}
        <nav className="hidden md:flex items-center gap-6">
          <Link to="/" className="text-gray-800 dark:text-gray-200 hover:text-blue-600 transition">Home</Link>
          <Link to="/vagas" className="text-gray-800 dark:text-gray-200 hover:text-blue-600 transition">Vagas</Link>
          <Link to="/login" className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition">Login</Link>
          <Link to="/register" className="px-4 py-2 bg-gray-200 dark:bg-gray-700 dark:text-white rounded hover:bg-gray-300 dark:hover:bg-gray-600 transition">Cadastre-se</Link>
          <button onClick={toggleDarkMode} className="p-2 rounded hover:bg-gray-100 dark:hover:bg-gray-800 transition">
            {darkMode ? <Sun className="w-5 h-5 text-yellow-400" /> : <Moon className="w-5 h-5 text-gray-600" />}
          </button>
        </nav>

        {/* Mobile Menu Button */}
        <div className="md:hidden flex items-center gap-2">
          <button onClick={toggleDarkMode} className="p-2 rounded hover:bg-gray-100 dark:hover:bg-gray-800 transition">
            {darkMode ? <Sun className="w-5 h-5 text-yellow-400" /> : <Moon className="w-5 h-5 text-gray-600" />}
          </button>
          <button onClick={() => setMenuOpen(!menuOpen)} className="p-2 rounded hover:bg-gray-100 dark:hover:bg-gray-800 transition">
            <Menu className="w-6 h-6 text-gray-800 dark:text-gray-200" />
          </button>
        </div>
      </div>

      {/* Mobile Dropdown */}
      {menuOpen && (
        <div className="md:hidden px-6 pb-4 flex flex-col gap-4 bg-white dark:bg-gray-900 shadow-sm border-b border-gray-300 dark:border-gray-700 transition-colors duration-300">
          <Link to="/" className="text-gray-800 dark:text-gray-200 hover:text-blue-600 transition">Home</Link>
          <Link to="/vagas" className="text-gray-800 dark:text-gray-200 hover:text-blue-600 transition">Vagas</Link>
          <Link to="/login" className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition text-center">Login</Link>
          <Link to="/register" className="px-4 py-2 bg-gray-200 dark:bg-gray-700 dark:text-white rounded hover:bg-gray-300 dark:hover:bg-gray-600 transition text-center">Cadastre-se</Link>
        </div>
      )}
    </header>
  );
}

export default Header;
