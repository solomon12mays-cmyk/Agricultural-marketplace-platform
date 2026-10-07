/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{ts,tsx}"],
  theme: {
    extend: {
      colors: {
        forest: "#173d2b",
        leaf: "#2f6847",
        cream: "#f7f8f4",
        clay: "#f1e6d2",
        brand: {
          deep: "#123F32",
          green: "#2F6B45",
          gold: "#D8A83E",
          orange: "#D97832",
          cream: "#F7F2E7",
          ink: "#17231E",
        },
      },
      fontFamily: {
        sans: ["Inter", "ui-sans-serif", "system-ui", "sans-serif"],
        display: [
          "Plus Jakarta Sans",
          "Inter",
          "ui-sans-serif",
          "system-ui",
          "sans-serif",
        ],
      },
      boxShadow: {
        card: "0 1px 2px rgba(23, 35, 30, 0.04), 0 18px 40px -24px rgba(18, 63, 50, 0.25)",
        pill: "0 10px 30px -12px rgba(217, 120, 50, 0.55)",
      },
    },
  },
  plugins: [],
};
