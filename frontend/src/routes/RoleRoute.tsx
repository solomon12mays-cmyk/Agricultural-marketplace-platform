import type { ReactNode } from "react";
import { Navigate } from "react-router-dom";
import { useAuthStore } from "../app/store/authStore";
import type { Role } from "../types/auth";
import { routePaths } from "./routePaths";

interface RoleRouteProps {
  roles: Role[];
  children: ReactNode;
}

/** Allows access only when the user holds at least one of the roles. */
export function RoleRoute({ roles, children }: RoleRouteProps) {
  const user = useAuthStore((state) => state.user);

  if (!user) {
    return <Navigate to={routePaths.login} replace />;
  }

  const allowed = roles.some((role) => user.roles.includes(role));
  if (!allowed) {
    // TODO: render a dedicated "no access" screen.
    return <Navigate to={routePaths.home} replace />;
  }

  return <>{children}</>;
}

export default RoleRoute;
