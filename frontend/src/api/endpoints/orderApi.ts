import { client } from "../client";

/**
 * order endpoints (base: /api/v1/orders).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const orderApi = {
  basePath: "/api/v1/orders",
  client,
};
