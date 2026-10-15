import { useAuthStore } from "../app/store/authStore";

/** Permission checks for the current user. */
export function usePermissions() {
  const permissions = useAuthStore((state) => state.user?.permissions ?? []);

  function has(permission: string): boolean {
    return permissions.includes(permission);
  }

  function hasAll(required: string[]): boolean {
    return required.every((permission) => permissions.includes(permission));
  }

  function hasAny(required: string[]): boolean {
    return required.some((permission) => permissions.includes(permission));
  }

  return { permissions, has, hasAll, hasAny };
}

export default usePermissions;
