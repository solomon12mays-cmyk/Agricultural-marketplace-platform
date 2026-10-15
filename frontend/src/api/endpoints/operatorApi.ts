import { client } from "../client";

/**
 * operator endpoints (base: /api/v1/operator).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const operatorApi = {
  basePath: "/api/v1/operator",
  client,
};
