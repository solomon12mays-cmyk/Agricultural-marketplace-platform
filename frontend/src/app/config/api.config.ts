import { appConfig } from "./app.config";

/** HTTP client configuration shared by every endpoint module. */
export const apiConfig = {
  baseUrl: appConfig.apiBaseUrl,
  timeoutMs: 15_000,
  contentType: "application/json",
  withCredentials: false,
} as const;
