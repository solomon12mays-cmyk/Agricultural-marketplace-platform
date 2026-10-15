import DashboardLayout from "../components/layout/DashboardLayout";
import { adminNavigation } from "../components/navigation/AdminNavigation";

/** Dashboard shell for administrator screens. */
export function AdminLayout() {
  return <DashboardLayout links={adminNavigation} title="Administration" />;
}

export default AdminLayout;
