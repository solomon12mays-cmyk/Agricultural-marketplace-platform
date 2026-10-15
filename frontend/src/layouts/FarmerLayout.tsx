import DashboardLayout from "../components/layout/DashboardLayout";
import { farmerNavigation } from "../components/navigation/FarmerNavigation";

/** Dashboard shell for farmer screens. */
export function FarmerLayout() {
  return <DashboardLayout links={farmerNavigation} title="Farmer" />;
}

export default FarmerLayout;
