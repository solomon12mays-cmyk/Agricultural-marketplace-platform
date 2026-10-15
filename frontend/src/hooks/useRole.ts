import { useAuthStore } from "../app/store/authStore";
import type { Role } from "../types/auth";

/** Role checks for the current user. */
export function useRole() {
  const roles = useAuthStore((state) => state.user?.roles ?? []);

  function isRole(role: Role): boolean {
    return roles.includes(role);
  }

  function isAnyRole(...candidates: Role[]): boolean {
    return candidates.some((role) => roles.includes(role));
  }

  const primaryRole: Role | undefined = roles[0];

  return { roles, primaryRole, isRole, isAnyRole };
}

export default useRole;
