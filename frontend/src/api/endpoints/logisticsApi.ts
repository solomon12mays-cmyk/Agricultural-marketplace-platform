import { client } from "../client";

/**
 * logistics endpoints (base: /api/v1/logistics).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const logisticsApi = {
  basePath: "/api/v1/logistics",
  client,
};
