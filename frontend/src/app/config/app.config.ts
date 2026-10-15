/** Runtime application configuration sourced from Vite env variables. */
export const appConfig = {
  name: import.meta.env.VITE_APP_NAME ?? "Agricultural Marketplace",
  apiBaseUrl: import.meta.env.VITE_API_BASE_URL ?? "/api/v1",
  defaultCurrency: "ETB",
  locale: "en-ET",
  defaultPageSize: 20,
} as const;
