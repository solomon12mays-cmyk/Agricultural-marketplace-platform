import type { NavigationItem } from "./RoleNavigation";

/** Sidebar links for administrator accounts. */
export const adminNavigation: NavigationItem[] = [
  { to: "/admin", label: "Dashboard" },
  { to: "/admin/users", label: "Users" },
  { to: "/admin/roles", label: "Roles" },
  { to: "/admin/permissions", label: "Permissions" },
  { to: "/admin/organizations", label: "Organizations" },
  { to: "/admin/farmers", label: "Farmers" },
  { to: "/admin/drivers", label: "Drivers" },
  { to: "/admin/orders", label: "Orders" },
  { to: "/admin/payments", label: "Payments" },
  { to: "/admin/payouts", label: "Payouts" },
  { to: "/admin/audit-logs", label: "Audit logs" },
  { to: "/admin/settings", label: "Settings" },
];
