import type { Role } from "../../types/auth";
import { buyerNavigation } from "./BuyerNavigation";
import { driverNavigation } from "./DriverNavigation";
import { farmerNavigation } from "./FarmerNavigation";
import { operatorNavigation } from "./OperatorNavigation";
import { organizationNavigation } from "./OrganizationNavigation";
import { adminNavigation } from "./AdminNavigation";

export interface NavigationItem {
  to: string;
  label: string;
}

const navigationByRole: Record<Role, NavigationItem[]> = {
  FARMER: farmerNavigation,
  ORGANIZATION: organizationNavigation,
  BUYER: buyerNavigation,
  DRIVER: driverNavigation,
  OPERATOR: operatorNavigation,
  ADMIN: adminNavigation,
};

/** Returns the navigation items for a role (defaults to buyer links). */
export function navigationForRole(role: Role | undefined): NavigationItem[] {
  if (!role) {
    return buyerNavigation;
  }
  return navigationByRole[role] ?? buyerNavigation;
}

/** Role-aware navigation block; renders nothing without a role. */
export function RoleNavigation({ role }: { role: Role | undefined }) {
  const items = navigationForRole(role);
  return (
    <ul className="space-y-1">
      {items.map((item) => (
        <li key={item.to} className="text-sm text-slate-600">
          {item.label}
        </li>
      ))}
    </ul>
  );
}

export default RoleNavigation;
