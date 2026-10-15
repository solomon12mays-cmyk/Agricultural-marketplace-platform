import { client } from "../client";

/**
 * admin endpoints (base: /api/v1/admin).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const adminApi = {
  basePath: "/api/v1/admin",
  client,
};
