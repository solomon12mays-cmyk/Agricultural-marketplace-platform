import DashboardLayout from "../components/layout/DashboardLayout";
import { organizationNavigation } from "../components/navigation/OrganizationNavigation";

/** Dashboard shell for organization screens. */
export function OrganizationLayout() {
  return <DashboardLayout links={organizationNavigation} title="Organization" />;
}

export default OrganizationLayout;
