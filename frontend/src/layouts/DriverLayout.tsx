import DashboardLayout from "../components/layout/DashboardLayout";
import { driverNavigation } from "../components/navigation/DriverNavigation";

/** Dashboard shell for driver screens. */
export function DriverLayout() {
  return <DashboardLayout links={driverNavigation} title="Driver" />;
}

export default DriverLayout;
