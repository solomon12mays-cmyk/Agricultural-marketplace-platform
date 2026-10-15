import { client } from "../client";

/**
 * payment endpoints (base: /api/v1/payments).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const paymentApi = {
  basePath: "/api/v1/payments",
  client,
};
