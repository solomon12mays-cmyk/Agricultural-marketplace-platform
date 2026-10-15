import { createElement, type ReactElement } from "react";
import { Outlet, type RouteObject } from "react-router-dom";
import AppLayout from "../components/layout/AppLayout";
import AuthLayout from "../components/layout/AuthLayout";
import { appRoutes, type AppRouteDefinition, type LayoutKey } from "../app/config/routes.config";
import AdminLayout from "../layouts/AdminLayout";
import BuyerLayout from "../layouts/BuyerLayout";
import DriverLayout from "../layouts/DriverLayout";
import FarmerLayout from "../layouts/FarmerLayout";
import OperatorLayout from "../layouts/OperatorLayout";
import OrganizationLayout from "../layouts/OrganizationLayout";
import PublicLayout from "../layouts/PublicLayout";
import PermissionRoute from "./PermissionRoute";
import ProtectedRoute from "./ProtectedRoute";
import RoleRoute from "./RoleRoute";

const layoutOrder: LayoutKey[] = [
  "public",
  "auth",
  "app",
  "farmer",
  "organization",
  "buyer",
  "driver",
  "operator",
  "admin",
];

function AuthLayoutRoute() {
  return (
    <AuthLayout>
      <Outlet />
    </AuthLayout>
  );
}

function AppLayoutRoute() {
  return (
    <AppLayout>
      <Outlet />
    </AppLayout>
  );
}

const layoutElements: Record<LayoutKey, ReactElement> = {
  public: <PublicLayout />,
  auth: <AuthLayoutRoute />,
  app: <AppLayoutRoute />,
  farmer: <FarmerLayout />,
  organization: <OrganizationLayout />,
  buyer: <BuyerLayout />,
  driver: <DriverLayout />,
  operator: <OperatorLayout />,
  admin: <AdminLayout />,
};

/** Wraps a route element with its access guards. */
function buildElement(definition: AppRouteDefinition): ReactElement {
  const page: ReactElement = createElement(definition.component);
  if (definition.permissions) {
    return <PermissionRoute permissions={definition.permissions}>{page}</PermissionRoute>;
  }
  if (definition.roles) {
    return <RoleRoute roles={definition.roles}>{page}</RoleRoute>;
  }
  if (definition.protected) {
    return <ProtectedRoute>{page}</ProtectedRoute>;
  }
  return page;
}

function NotFoundPage() {
  return (
    <div className="page-container">
      <h1 className="page-title">Page not found</h1>
      <p className="page-subtitle">The page you are looking for does not exist.</p>
    </div>
  );
}

function groupRoutes(): RouteObject[] {
  const objects: RouteObject[] = [];
  for (const layout of layoutOrder) {
    const definitions = appRoutes.filter((definition) => definition.layout === layout);
    if (definitions.length === 0) {
      continue;
    }
    objects.push({
      element: layoutElements[layout],
      children: definitions.map((definition) => ({
        path: definition.path,
        element: buildElement(definition),
      })),
    });
  }
  return objects;
}

/** React Router route objects rendered by `AppRouter`. */
export const routeObjects: RouteObject[] = [...groupRoutes(), { path: "*", element: <NotFoundPage /> }];
