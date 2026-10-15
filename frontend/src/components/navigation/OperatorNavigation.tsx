import type { NavigationItem } from "./RoleNavigation";

/** Sidebar links for operator accounts. */
export const operatorNavigation: NavigationItem[] = [
  { to: "/operator", label: "Dashboard" },
  { to: "/operator/tasks", label: "Tasks" },
  { to: "/operator/verifications", label: "Verifications" },
  { to: "/logistics", label: "Logistics" },
  { to: "/compliance", label: "Compliance" },
  { to: "/compliance/disputes", label: "Disputes" },
  { to: "/notifications", label: "Notifications" },
];
