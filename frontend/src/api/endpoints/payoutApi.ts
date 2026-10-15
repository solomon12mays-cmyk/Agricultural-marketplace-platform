import { client } from "../client";

/**
 * payout endpoints (base: /api/v1/payouts).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const payoutApi = {
  basePath: "/api/v1/payouts",
  client,
};
