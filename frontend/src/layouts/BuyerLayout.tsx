import DashboardLayout from "../components/layout/DashboardLayout";
import { buyerNavigation } from "../components/navigation/BuyerNavigation";

/** Dashboard shell for personal buyer screens. */
export function BuyerLayout() {
  return <DashboardLayout links={buyerNavigation} title="Buyer" />;
}

export default BuyerLayout;
