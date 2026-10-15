import { client } from "../client";

/**
 * pickup endpoints (base: /api/v1/pickups).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const pickupApi = {
  basePath: "/api/v1/pickups",
  client,
};
