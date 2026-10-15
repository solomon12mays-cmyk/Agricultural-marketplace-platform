import { client } from "../client";

/**
 * farmer endpoints (base: /api/v1/farmers).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const farmerApi = {
  basePath: "/api/v1/farmers",
  client,
};
