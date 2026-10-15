import { client } from "../client";

/**
 * compliance endpoints (base: /api/v1/compliance).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const complianceApi = {
  basePath: "/api/v1/compliance",
  client,
};
