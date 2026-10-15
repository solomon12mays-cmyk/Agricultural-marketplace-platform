import type { Role } from "../types/auth";

/** All assignable platform roles. */
export const ROLES = {
  FARMER: "FARMER",
  ORGANIZATION: "ORGANIZATION",
  BUYER: "BUYER",
  DRIVER: "DRIVER",
  OPERATOR: "OPERATOR",
  ADMIN: "ADMIN",
} as const satisfies Record<Role, Role>;

export const ROLE_LABELS: Record<Role, string> = {
  FARMER: "Farmer",
  ORGANIZATION: "Organization",
  BUYER: "Buyer",
  DRIVER: "Driver",
  OPERATOR: "Operator",
  ADMIN: "Administrator",
};
