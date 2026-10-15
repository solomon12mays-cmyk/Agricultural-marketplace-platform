import { client } from "../client";

/**
 * delivery endpoints (base: /api/v1/deliveries).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const deliveryApi = {
  basePath: "/api/v1/deliveries",
  client,
};
