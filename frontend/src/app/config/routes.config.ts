import type { ComponentType } from "react";
import { PERMISSIONS } from "../../constants/permissions";
import { routePaths } from "../../routes/routePaths";
import type { Role } from "../../types/auth";

import RoleSelectionPage from "../../features/auth/pages/RoleSelectionPage";
import LoginPage from "../../features/auth/pages/LoginPage";
import RegisterPage from "../../features/auth/pages/RegisterPage";
import ActivateAccountPage from "../../features/auth/pages/ActivateAccountPage";
import ChangePasswordPage from "../../features/auth/pages/ChangePasswordPage";
import ForgotPasswordPage from "../../features/auth/pages/ForgotPasswordPage";
import AccountLockedPage from "../../features/auth/pages/AccountLockedPage";

import MarketplacePage from "../../features/marketplace/pages/MarketplacePage";
import ProductSearchPage from "../../features/marketplace/pages/ProductSearchPage";
import MarketplaceListingPage from "../../features/marketplace/pages/MarketplaceListingPage";

import FarmerDashboardPage from "../../features/farmer/pages/FarmerDashboardPage";
import FarmerProfilePage from "../../features/farmer/pages/FarmerProfilePage";
import FarmerVerificationPage from "../../features/farmer/pages/FarmerVerificationPage";
import FarmerOrdersPage from "../../features/farmer/pages/FarmerOrdersPage";
import FarmerPaymentsPage from "../../features/farmer/pages/FarmerPaymentsPage";
import FarmerSettingsPage from "../../features/farmer/pages/FarmerSettingsPage";

import FarmsPage from "../../features/farm/pages/FarmsPage";
import CreateFarmPage from "../../features/farm/pages/CreateFarmPage";
import EditFarmPage from "../../features/farm/pages/EditFarmPage";
import FarmDetailsPage from "../../features/farm/pages/FarmDetailsPage";

import InventoryPage from "../../features/inventory/pages/InventoryPage";
import InventoryDetailsPage from "../../features/inventory/pages/InventoryDetailsPage";
import InventoryHistoryPage from "../../features/inventory/pages/InventoryHistoryPage";

import ListingsPage from "../../features/listing/pages/ListingsPage";
import CreateListingPage from "../../features/listing/pages/CreateListingPage";
import EditListingPage from "../../features/listing/pages/EditListingPage";
import ListingDetailsPage from "../../features/listing/pages/ListingDetailsPage";

import OrganizationDashboardPage from "../../features/organization/pages/OrganizationDashboardPage";
import OrganizationRegistrationPage from "../../features/organization/pages/OrganizationRegistrationPage";
import OrganizationVerificationPage from "../../features/organization/pages/OrganizationVerificationPage";
import OrganizationProfilePage from "../../features/organization/pages/OrganizationProfilePage";
import OrganizationOrdersPage from "../../features/organization/pages/OrganizationOrdersPage";
import OrganizationPaymentsPage from "../../features/organization/pages/OrganizationPaymentsPage";
import OrganizationSettingsPage from "../../features/organization/pages/OrganizationSettingsPage";

import BuyerDashboardPage from "../../features/personal-buyer/pages/BuyerDashboardPage";
import BuyerProfilePage from "../../features/personal-buyer/pages/BuyerProfilePage";
import BuyerOrdersPage from "../../features/personal-buyer/pages/BuyerOrdersPage";
import BuyerPaymentsPage from "../../features/personal-buyer/pages/BuyerPaymentsPage";
import BuyerSettingsPage from "../../features/personal-buyer/pages/BuyerSettingsPage";

import OrdersPage from "../../features/order/pages/OrdersPage";
import CreateOrderPage from "../../features/order/pages/CreateOrderPage";
import OrderDetailsPage from "../../features/order/pages/OrderDetailsPage";
import OrderTrackingPage from "../../features/order/pages/OrderTrackingPage";

import PaymentPage from "../../features/payment/pages/PaymentPage";
import PaymentPendingPage from "../../features/payment/pages/PaymentPendingPage";
import PaymentSuccessPage from "../../features/payment/pages/PaymentSuccessPage";
import PaymentFailedPage from "../../features/payment/pages/PaymentFailedPage";
import PaymentHistoryPage from "../../features/payment/pages/PaymentHistoryPage";

import DriverDashboardPage from "../../features/driver/pages/DriverDashboardPage";
import DriverProfilePage from "../../features/driver/pages/DriverProfilePage";
import DriverVerificationPage from "../../features/driver/pages/DriverVerificationPage";
import DriverAssignmentsPage from "../../features/driver/pages/DriverAssignmentsPage";
import DriverTripPage from "../../features/driver/pages/DriverTripPage";
import DriverPaymentsPage from "../../features/driver/pages/DriverPaymentsPage";
import DriverSettingsPage from "../../features/driver/pages/DriverSettingsPage";

import LogisticsPage from "../../features/logistics/pages/LogisticsPage";
import DriverSelectionPage from "../../features/logistics/pages/DriverSelectionPage";
import TripTrackingPage from "../../features/logistics/pages/TripTrackingPage";

import NotificationsPage from "../../features/notification/pages/NotificationsPage";

import OperatorDashboardPage from "../../features/operator/pages/OperatorDashboardPage";
import OperatorTasksPage from "../../features/operator/pages/OperatorTasksPage";
import OperatorTaskDetailsPage from "../../features/operator/pages/OperatorTaskDetailsPage";
import VerificationTasksPage from "../../features/operator/pages/VerificationTasksPage";

import ComplianceCasesPage from "../../features/compliance/pages/ComplianceCasesPage";
import ComplianceCaseDetailsPage from "../../features/compliance/pages/ComplianceCaseDetailsPage";
import DisputesPage from "../../features/compliance/pages/DisputesPage";
import DisputeDetailsPage from "../../features/compliance/pages/DisputeDetailsPage";

import AdminDashboardPage from "../../features/admin/pages/AdminDashboardPage";
import UsersPage from "../../features/admin/pages/UsersPage";
import RolesPage from "../../features/admin/pages/RolesPage";
import PermissionsPage from "../../features/admin/pages/PermissionsPage";
import OrganizationsPage from "../../features/admin/pages/OrganizationsPage";
import FarmersPage from "../../features/admin/pages/FarmersPage";
import DriversPage from "../../features/admin/pages/DriversPage";
import AdminOrdersPage from "../../features/admin/pages/OrdersPage";
import AdminPaymentsPage from "../../features/admin/pages/PaymentsPage";
import PayoutsPage from "../../features/admin/pages/PayoutsPage";
import AuditLogsPage from "../../features/admin/pages/AuditLogsPage";
import SystemSettingsPage from "../../features/admin/pages/SystemSettingsPage";

/** Route group layouts. */
export type LayoutKey =
  | "public"
  | "auth"
  | "app"
  | "farmer"
  | "organization"
  | "buyer"
  | "driver"
  | "operator"
  | "admin";

export interface AppRouteDefinition {
  path: string;
  component: ComponentType;
  layout: LayoutKey;
  /** Requires an authenticated user. */
  protected?: boolean;
  /** Requires at least one of these roles (implies protected). */
  roles?: Role[];
  /** Requires all of these permissions (implies protected). */
  permissions?: string[];
}

const producerRoles: Role[] = ["FARMER", "ORGANIZATION"];
const staffRoles: Role[] = ["OPERATOR", "ADMIN"];

/** Every application route: path, page, layout and access rules. */
export const appRoutes: AppRouteDefinition[] = [
  // Public marketplace
  { path: routePaths.home, component: MarketplacePage, layout: "public" },
  { path: routePaths.marketplaceSearch, component: ProductSearchPage, layout: "public" },
  { path: routePaths.marketplaceListing, component: MarketplaceListingPage, layout: "public" },

  // Auth (public)
  { path: routePaths.roleSelection, component: RoleSelectionPage, layout: "auth" },
  { path: routePaths.login, component: LoginPage, layout: "auth" },
  { path: routePaths.register, component: RegisterPage, layout: "auth" },
  { path: routePaths.activate, component: ActivateAccountPage, layout: "auth" },
  { path: routePaths.forgotPassword, component: ForgotPasswordPage, layout: "auth" },
  { path: routePaths.accountLocked, component: AccountLockedPage, layout: "auth" },
  { path: routePaths.changePassword, component: ChangePasswordPage, layout: "auth", protected: true },

  // Notifications (any authenticated user)
  { path: routePaths.notifications, component: NotificationsPage, layout: "app", protected: true },

  // Farmer
  { path: routePaths.farmerDashboard, component: FarmerDashboardPage, layout: "farmer", roles: ["FARMER"] },
  { path: routePaths.farmerProfile, component: FarmerProfilePage, layout: "farmer", roles: ["FARMER"] },
  { path: routePaths.farmerVerification, component: FarmerVerificationPage, layout: "farmer", roles: ["FARMER"] },
  { path: routePaths.farmerOrders, component: FarmerOrdersPage, layout: "farmer", roles: ["FARMER"] },
  { path: routePaths.farmerPayments, component: FarmerPaymentsPage, layout: "farmer", roles: ["FARMER"] },
  { path: routePaths.farmerSettings, component: FarmerSettingsPage, layout: "farmer", roles: ["FARMER"] },

  // Farms
  { path: routePaths.farms, component: FarmsPage, layout: "farmer", roles: producerRoles },
  { path: routePaths.farmCreate, component: CreateFarmPage, layout: "farmer", roles: producerRoles },
  { path: routePaths.farmEdit, component: EditFarmPage, layout: "farmer", roles: producerRoles },
  { path: routePaths.farmDetails, component: FarmDetailsPage, layout: "farmer", roles: producerRoles },

  // Inventory
  { path: routePaths.inventory, component: InventoryPage, layout: "farmer", roles: producerRoles },
  { path: routePaths.inventoryHistory, component: InventoryHistoryPage, layout: "farmer", roles: producerRoles },
  { path: routePaths.inventoryDetails, component: InventoryDetailsPage, layout: "farmer", roles: producerRoles },

  // Listings
  { path: routePaths.listings, component: ListingsPage, layout: "farmer", roles: producerRoles },
  { path: routePaths.listingCreate, component: CreateListingPage, layout: "farmer", roles: producerRoles },
  { path: routePaths.listingEdit, component: EditListingPage, layout: "farmer", roles: producerRoles },
  { path: routePaths.listingDetails, component: ListingDetailsPage, layout: "farmer", roles: producerRoles },

  // Organization
  { path: routePaths.organizationDashboard, component: OrganizationDashboardPage, layout: "organization", roles: ["ORGANIZATION"] },
  { path: routePaths.organizationRegistration, component: OrganizationRegistrationPage, layout: "organization", roles: ["ORGANIZATION"] },
  { path: routePaths.organizationVerification, component: OrganizationVerificationPage, layout: "organization", roles: ["ORGANIZATION"] },
  { path: routePaths.organizationProfile, component: OrganizationProfilePage, layout: "organization", roles: ["ORGANIZATION"] },
  { path: routePaths.organizationOrders, component: OrganizationOrdersPage, layout: "organization", roles: ["ORGANIZATION"] },
  { path: routePaths.organizationPayments, component: OrganizationPaymentsPage, layout: "organization", roles: ["ORGANIZATION"] },
  { path: routePaths.organizationSettings, component: OrganizationSettingsPage, layout: "organization", roles: ["ORGANIZATION"] },

  // Buyer
  { path: routePaths.buyerDashboard, component: BuyerDashboardPage, layout: "buyer", roles: ["BUYER"] },
  { path: routePaths.buyerProfile, component: BuyerProfilePage, layout: "buyer", roles: ["BUYER"] },
  { path: routePaths.buyerOrders, component: BuyerOrdersPage, layout: "buyer", roles: ["BUYER"] },
  { path: routePaths.buyerPayments, component: BuyerPaymentsPage, layout: "buyer", roles: ["BUYER"] },
  { path: routePaths.buyerSettings, component: BuyerSettingsPage, layout: "buyer", roles: ["BUYER"] },

  // Orders
  { path: routePaths.orders, component: OrdersPage, layout: "buyer", roles: ["BUYER"] },
  { path: routePaths.orderCreate, component: CreateOrderPage, layout: "buyer", roles: ["BUYER"] },
  { path: routePaths.orderDetails, component: OrderDetailsPage, layout: "buyer", roles: ["BUYER"] },
  { path: routePaths.orderTracking, component: OrderTrackingPage, layout: "buyer", roles: ["BUYER"] },

  // Payments
  { path: routePaths.paymentCheckout, component: PaymentPage, layout: "buyer", roles: ["BUYER"] },
  { path: routePaths.paymentPending, component: PaymentPendingPage, layout: "buyer", roles: ["BUYER"] },
  { path: routePaths.paymentSuccess, component: PaymentSuccessPage, layout: "buyer", roles: ["BUYER"] },
  { path: routePaths.paymentFailed, component: PaymentFailedPage, layout: "buyer", roles: ["BUYER"] },
  { path: routePaths.paymentHistory, component: PaymentHistoryPage, layout: "buyer", roles: ["BUYER"] },

  // Driver
  { path: routePaths.driverDashboard, component: DriverDashboardPage, layout: "driver", roles: ["DRIVER"] },
  { path: routePaths.driverProfile, component: DriverProfilePage, layout: "driver", roles: ["DRIVER"] },
  { path: routePaths.driverVerification, component: DriverVerificationPage, layout: "driver", roles: ["DRIVER"] },
  { path: routePaths.driverAssignments, component: DriverAssignmentsPage, layout: "driver", roles: ["DRIVER"] },
  { path: routePaths.driverTrip, component: DriverTripPage, layout: "driver", roles: ["DRIVER"] },
  { path: routePaths.driverPayments, component: DriverPaymentsPage, layout: "driver", roles: ["DRIVER"] },
  { path: routePaths.driverSettings, component: DriverSettingsPage, layout: "driver", roles: ["DRIVER"] },

  // Logistics (operator)
  { path: routePaths.logistics, component: LogisticsPage, layout: "operator", roles: staffRoles },
  { path: routePaths.logisticsDriverSelection, component: DriverSelectionPage, layout: "operator", roles: staffRoles },
  { path: routePaths.logisticsTripTracking, component: TripTrackingPage, layout: "operator", roles: staffRoles },

  // Operator
  { path: routePaths.operatorDashboard, component: OperatorDashboardPage, layout: "operator", roles: staffRoles },
  { path: routePaths.operatorTasks, component: OperatorTasksPage, layout: "operator", roles: staffRoles },
  { path: routePaths.operatorTaskDetails, component: OperatorTaskDetailsPage, layout: "operator", roles: staffRoles },
  { path: routePaths.operatorVerifications, component: VerificationTasksPage, layout: "operator", roles: staffRoles },

  // Compliance
  { path: routePaths.complianceCases, component: ComplianceCasesPage, layout: "operator", roles: staffRoles },
  { path: routePaths.complianceCaseDetails, component: ComplianceCaseDetailsPage, layout: "operator", roles: staffRoles },
  { path: routePaths.complianceDisputes, component: DisputesPage, layout: "operator", roles: staffRoles },
  { path: routePaths.disputeDetails, component: DisputeDetailsPage, layout: "operator", roles: staffRoles },

  // Admin
  { path: routePaths.adminDashboard, component: AdminDashboardPage, layout: "admin", roles: ["ADMIN"] },
  { path: routePaths.adminUsers, component: UsersPage, layout: "admin", roles: ["ADMIN"] },
  { path: routePaths.adminRoles, component: RolesPage, layout: "admin", roles: ["ADMIN"] },
  { path: routePaths.adminPermissions, component: PermissionsPage, layout: "admin", roles: ["ADMIN"] },
  { path: routePaths.adminOrganizations, component: OrganizationsPage, layout: "admin", roles: ["ADMIN"] },
  { path: routePaths.adminFarmers, component: FarmersPage, layout: "admin", roles: ["ADMIN"] },
  { path: routePaths.adminDrivers, component: DriversPage, layout: "admin", roles: ["ADMIN"] },
  { path: routePaths.adminOrders, component: AdminOrdersPage, layout: "admin", roles: ["ADMIN"] },
  { path: routePaths.adminPayments, component: AdminPaymentsPage, layout: "admin", roles: ["ADMIN"] },
  { path: routePaths.adminPayouts, component: PayoutsPage, layout: "admin", roles: ["ADMIN"] },
  {
    path: routePaths.adminAuditLogs,
    component: AuditLogsPage,
    layout: "admin",
    permissions: [PERMISSIONS.AUDIT_VIEW],
  },
  { path: routePaths.adminSettings, component: SystemSettingsPage, layout: "admin", roles: ["ADMIN"] },
];
