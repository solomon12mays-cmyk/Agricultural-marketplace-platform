import { client } from "../client";

/**
 * farm endpoints (base: /api/v1/farms).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const farmApi = {
  basePath: "/api/v1/farms",
  client,
};
