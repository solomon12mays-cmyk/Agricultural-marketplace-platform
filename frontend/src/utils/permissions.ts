import { PERMISSIONS, type PermissionCode } from "../constants/permissions";
import type { Role } from "../types/auth";

/** Role → permission grants used for optimistic UI checks (server enforces). */
const roleGrants: Record<Role, PermissionCode[]> = {
  FARMER: [PERMISSIONS.LISTING_CREATE, PERMISSIONS.LISTING_MANAGE, PERMISSIONS.INVENTORY_MANAGE, PERMISSIONS.ORDER_CREATE],
  ORGANIZATION: [PERMISSIONS.LISTING_CREATE, PERMISSIONS.LISTING_MANAGE, PERMISSIONS.INVENTORY_MANAGE, PERMISSIONS.ORDER_CREATE],
  BUYER: [PERMISSIONS.ORDER_CREATE, PERMISSIONS.PAYMENT_INITIATE],
  DRIVER: [],
  OPERATOR: [PERMISSIONS.FARMER_VERIFY, PERMISSIONS.ORGANIZATION_VERIFY, PERMISSIONS.TASK_MANAGE, PERMISSIONS.DISPUTE_HANDLE],
  ADMIN: Object.values(PERMISSIONS),
};

/** Permissions a role holds before considering explicit grants. */
export function permissionsForRoles(roles: Role[]): string[] {
  const granted = new Set<string>();
  for (const role of roles) {
    for (const permission of roleGrants[role] ?? []) {
      granted.add(permission);
    }
  }
  return [...granted];
}

/** Whether the given permission list satisfies a requirement. */
export function hasPermission(granted: string[], required: string): boolean {
  return granted.includes(required);
}
