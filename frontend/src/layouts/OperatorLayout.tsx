import DashboardLayout from "../components/layout/DashboardLayout";
import { operatorNavigation } from "../components/navigation/OperatorNavigation";

/** Dashboard shell for operator screens. */
export function OperatorLayout() {
  return <DashboardLayout links={operatorNavigation} title="Operator" />;
}

export default OperatorLayout;
