import { client } from "../client";

/**
 * notification endpoints (base: /api/v1/notifications).
 *
 * TODO: implement typed operations (list, get, create, update, delete)
 * using the shared `client`.
 */
export const notificationApi = {
  basePath: "/api/v1/notifications",
  client,
};
