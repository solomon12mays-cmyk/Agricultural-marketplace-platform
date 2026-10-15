import type { NavigationItem } from "./RoleNavigation";

/** Sidebar links for driver accounts. */
export const driverNavigation: NavigationItem[] = [
  { to: "/driver", label: "Dashboard" },
  { to: "/driver/profile", label: "Profile" },
  { to: "/driver/verification", label: "Verification" },
  { to: "/driver/assignments", label: "Assignments" },
  { to: "/driver/trip", label: "Current trip" },
  { to: "/driver/payments", label: "Earnings" },
  { to: "/notifications", label: "Notifications" },
  { to: "/driver/settings", label: "Settings" },
];
