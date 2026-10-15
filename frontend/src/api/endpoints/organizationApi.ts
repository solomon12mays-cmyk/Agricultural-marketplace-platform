import { client } from "../client";

/**
 * organization endpoints (base: /api/v1/organizations).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const organizationApi = {
  basePath: "/api/v1/organizations",
  client,
};
