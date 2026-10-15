import { client } from "../client";

/**
 * personal Buyer endpoints (base: /api/v1/personal-buyers).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const personalBuyerApi = {
  basePath: "/api/v1/personal-buyers",
  client,
};
