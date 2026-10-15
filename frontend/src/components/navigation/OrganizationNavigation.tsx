import type { NavigationItem } from "./RoleNavigation";

/** Sidebar links for organization accounts. */
export const organizationNavigation: NavigationItem[] = [
  { to: "/organization", label: "Dashboard" },
  { to: "/organization/profile", label: "Profile" },
  { to: "/organization/verification", label: "Verification" },
  { to: "/listings", label: "Listings" },
  { to: "/organization/orders", label: "Orders" },
  { to: "/organization/payments", label: "Payments" },
  { to: "/notifications", label: "Notifications" },
  { to: "/organization/settings", label: "Settings" },
];
