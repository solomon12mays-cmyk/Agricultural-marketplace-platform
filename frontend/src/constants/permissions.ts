/** Permission codes mirroring the backend's permission catalog. */
export const PERMISSIONS = {
  LISTING_CREATE: "listing:create",
  LISTING_MANAGE: "listing:manage",
  ORDER_CREATE: "order:create",
  ORDER_MANAGE: "order:manage",
  PAYMENT_INITIATE: "payment:initiate",
  INVENTORY_MANAGE: "inventory:manage",
  FARMER_VERIFY: "farmer:verify",
  ORGANIZATION_VERIFY: "organization:verify",
  TASK_MANAGE: "task:manage",
  DISPUTE_HANDLE: "dispute:handle",
  PAYOUT_MANAGE: "payout:manage",
  USER_MANAGE: "user:manage",
  AUDIT_VIEW: "audit:view",
} as const;

export type PermissionCode = (typeof PERMISSIONS)[keyof typeof PERMISSIONS];
