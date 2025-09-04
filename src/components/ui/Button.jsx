// src/components/ui/Button.jsx
export default function Button({
  children,
  variant = "primary",
  disabled = false,
  className = "",
  ...props
}) {
  const baseStyles = "px-4 py-2 rounded transition font-semibold text-center";

  const variants = {
    primary: "bg-blue-600 text-white hover:bg-blue-700",
    secondary: "bg-gray-200 text-gray-800 hover:bg-gray-300 dark:bg-gray-700 dark:text-white dark:hover:bg-gray-600",
    danger: "bg-red-600 text-white hover:bg-red-700",
    ghost: "bg-transparent text-blue-600 hover:bg-blue-50 dark:hover:bg-gray-800",
    disabled: "bg-gray-400 text-gray-700 cursor-not-allowed",
  };

  const variantStyles = disabled ? variants.disabled : variants[variant] || variants.primary;

  return (
    <button
      className={`${baseStyles} ${variantStyles} ${className}`}
      disabled={disabled}
      {...props}
    >
      {children}
    </button>
  );
}
