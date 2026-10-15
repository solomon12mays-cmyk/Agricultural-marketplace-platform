import axios from "axios";
import { apiConfig } from "../app/config/api.config";
import { registerRequestInterceptor, registerResponseInterceptor } from "./interceptors";

/**
 * Shared axios instance. Endpoint modules import this client and expose
 * typed helpers built on it.
 */
export const client = axios.create({
  baseURL: apiConfig.baseUrl,
  timeout: apiConfig.timeoutMs,
  headers: { "Content-Type": apiConfig.contentType },
  withCredentials: apiConfig.withCredentials,
});

registerRequestInterceptor(client);
registerResponseInterceptor(client);
