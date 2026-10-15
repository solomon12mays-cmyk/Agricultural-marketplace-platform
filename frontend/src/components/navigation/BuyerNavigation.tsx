import type { NavigationItem } from "./RoleNavigation";

/** Sidebar links for personal buyer accounts. */
export const buyerNavigation: NavigationItem[] = [
  { to: "/", label: "Marketplace" },
  { to: "/marketplace/search", label: "Search" },
  { to: "/buyer", label: "Dashboard" },
  { to: "/buyer/profile", label: "Profile" },
  { to: "/orders", label: "Orders" },
  { to: "/payments", label: "Payments" },
  { to: "/notifications", label: "Notifications" },
  { to: "/buyer/settings", label: "Settings" },
];
