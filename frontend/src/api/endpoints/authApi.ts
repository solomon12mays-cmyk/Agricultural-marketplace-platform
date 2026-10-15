import { client } from "../client";

/**
 * auth endpoints (base: /api/v1/auth).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const authApi = {
  basePath: "/api/v1/auth",
  client,
};
