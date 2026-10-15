import { useEffect, type ReactNode } from "react";
import { useAuthStore } from "./store/authStore";

/**
 * Runs one-time bootstrap work before (or while) the router renders:
 * restoring the stored session and validating it against the API.
 *
 * TODO: refresh the access token / fetch `/auth/me` when the API supports it.
 */
export default function AppInitializer({ children }: { children: ReactNode }) {
  const hydrate = useAuthStore((state) => state.hydrate);

  useEffect(() => {
    hydrate();
  }, [hydrate]);

  return <>{children}</>;
}
