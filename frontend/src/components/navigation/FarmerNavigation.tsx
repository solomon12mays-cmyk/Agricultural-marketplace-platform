import type { NavigationItem } from "./RoleNavigation";

/** Sidebar links for farmer accounts. */
export const farmerNavigation: NavigationItem[] = [
  { to: "/farmer", label: "Dashboard" },
  { to: "/farmer/profile", label: "Profile" },
  { to: "/farmer/verification", label: "Verification" },
  { to: "/farms", label: "Farms" },
  { to: "/inventory", label: "Inventory" },
  { to: "/listings", label: "Listings" },
  { to: "/farmer/orders", label: "Orders" },
  { to: "/farmer/payments", label: "Payments" },
  { to: "/notifications", label: "Notifications" },
  { to: "/farmer/settings", label: "Settings" },
];
