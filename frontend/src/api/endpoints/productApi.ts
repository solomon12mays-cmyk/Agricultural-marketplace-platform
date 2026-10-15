import { client } from "../client";

/**
 * product endpoints (base: /api/v1/products).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const productApi = {
  basePath: "/api/v1/products",
  client,
};
