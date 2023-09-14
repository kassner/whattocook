/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/main/js/**/*.{ts,tsx}",
    "./src/main/resources/templates/**/*.html"
  ],
  theme: {
    extend: {},
  },
  plugins: [
    require("@tailwindcss/typography"),
    require("daisyui"),
  ],
}
