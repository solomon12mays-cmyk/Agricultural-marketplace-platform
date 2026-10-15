import { client } from "../client";

/**
 * listing endpoints (base: /api/v1/listings).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const listingApi = {
  basePath: "/api/v1/listings",
  client,
};
