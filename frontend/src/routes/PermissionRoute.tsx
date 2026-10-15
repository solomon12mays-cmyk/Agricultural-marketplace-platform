import type { ReactNode } from "react";
import { Navigate } from "react-router-dom";
import { useAuthStore } from "../app/store/authStore";
import { routePaths } from "./routePaths";

interface PermissionRouteProps {
  permissions: string[];
  children: ReactNode;
}

/** Allows access only when the user holds every required permission. */
export function PermissionRoute({ permissions, children }: PermissionRouteProps) {
  const user = useAuthStore((state) => state.user);

  if (!user) {
    return <Navigate to={routePaths.login} replace />;
  }

  const hasAll = permissions.every((permission) => user.permissions.includes(permission));
  if (!hasAll) {
    return <Navigate to={routePaths.home} replace />;
  }

  return <>{children}</>;
}

export default PermissionRoute;
