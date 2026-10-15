/** Central registry of every route path in the application. */
export const routePaths = {
  // Public marketplace
  home: "/",
  marketplaceSearch: "/marketplace/search",
  marketplaceListing: "/marketplace/listings/:listingId",

  // Auth
  roleSelection: "/auth",
  login: "/auth/login",
  register: "/auth/register",
  activate: "/auth/activate",
  changePassword: "/auth/change-password",
  forgotPassword: "/auth/forgot-password",
  accountLocked: "/auth/account-locked",

  // Farmer
  farmerDashboard: "/farmer",
  farmerProfile: "/farmer/profile",
  farmerVerification: "/farmer/verification",
  farmerOrders: "/farmer/orders",
  farmerPayments: "/farmer/payments",
  farmerSettings: "/farmer/settings",

  // Farms
  farms: "/farms",
  farmCreate: "/farms/new",
  farmEdit: "/farms/:farmId/edit",
  farmDetails: "/farms/:farmId",

  // Inventory
  inventory: "/inventory",
  inventoryDetails: "/inventory/:inventoryId",
  inventoryHistory: "/inventory/history",

  // Listings
  listings: "/listings",
  listingCreate: "/listings/new",
  listingEdit: "/listings/:listingId/edit",
  listingDetails: "/listings/:listingId",

  // Organization
  organizationDashboard: "/organization",
  organizationRegistration: "/organization/register",
  organizationVerification: "/organization/verification",
  organizationProfile: "/organization/profile",
  organizationOrders: "/organization/orders",
  organizationPayments: "/organization/payments",
  organizationSettings: "/organization/settings",

  // Buyer
  buyerDashboard: "/buyer",
  buyerProfile: "/buyer/profile",
  buyerOrders: "/buyer/orders",
  buyerPayments: "/buyer/payments",
  buyerSettings: "/buyer/settings",

  // Orders (shared buyer flows)
  orders: "/orders",
  orderCreate: "/orders/new",
  orderDetails: "/orders/:orderId",
  orderTracking: "/orders/:orderId/track",

  // Payments
  paymentCheckout: "/pay/:orderId",
  paymentPending: "/pay/pending",
  paymentSuccess: "/pay/success",
  paymentFailed: "/pay/failed",
  paymentHistory: "/payments",

  // Driver
  driverDashboard: "/driver",
  driverProfile: "/driver/profile",
  driverVerification: "/driver/verification",
  driverAssignments: "/driver/assignments",
  driverTrip: "/driver/trip",
  driverPayments: "/driver/payments",
  driverSettings: "/driver/settings",

  // Logistics (operator)
  logistics: "/logistics",
  logisticsDriverSelection: "/logistics/drivers",
  logisticsTripTracking: "/logistics/trip",

  // Notifications
  notifications: "/notifications",

  // Operator
  operatorDashboard: "/operator",
  operatorTasks: "/operator/tasks",
  operatorTaskDetails: "/operator/tasks/:taskId",
  operatorVerifications: "/operator/verifications",

  // Compliance
  complianceCases: "/compliance",
  complianceCaseDetails: "/compliance/cases/:caseId",
  complianceDisputes: "/compliance/disputes",
  disputeDetails: "/compliance/disputes/:disputeId",

  // Admin
  adminDashboard: "/admin",
  adminUsers: "/admin/users",
  adminRoles: "/admin/roles",
  adminPermissions: "/admin/permissions",
  adminOrganizations: "/admin/organizations",
  adminFarmers: "/admin/farmers",
  adminDrivers: "/admin/drivers",
  adminOrders: "/admin/orders",
  adminPayments: "/admin/payments",
  adminPayouts: "/admin/payouts",
  adminAuditLogs: "/admin/audit-logs",
  adminSettings: "/admin/settings",
} as const;
