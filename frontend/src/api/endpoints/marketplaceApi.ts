import { client } from "../client";

/**
 * marketplace endpoints (base: /api/v1/marketplace).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const marketplaceApi = {
  basePath: "/api/v1/marketplace",
  client,
};
