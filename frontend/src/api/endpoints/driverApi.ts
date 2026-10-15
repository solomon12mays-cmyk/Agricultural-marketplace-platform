import { client } from "../client";

/**
 * driver endpoints (base: /api/v1/drivers).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const driverApi = {
  basePath: "/api/v1/drivers",
  client,
};
