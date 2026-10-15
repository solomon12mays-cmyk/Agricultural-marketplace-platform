import { useRoutes } from "react-router-dom";
import { routeObjects } from "../routes/routes";

/**
 * Renders the configured route table. Lives inside `AppProviders`, so the
 * browser router context is always available.
 */
export default function AppRouter() {
  return useRoutes(routeObjects);
}
