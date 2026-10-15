import { client } from "../client";

/**
 * inventory endpoints (base: /api/v1/inventory).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const inventoryApi = {
  basePath: "/api/v1/inventory",
  client,
};
