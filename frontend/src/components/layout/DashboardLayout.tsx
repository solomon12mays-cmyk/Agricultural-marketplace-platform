import { Outlet } from "react-router-dom";
import Header from "./Header";
import MobileNavigation from "./MobileNavigation";
import Sidebar, { type SidebarLink } from "./Sidebar";

interface DashboardLayoutProps {
  links: SidebarLink[];
  title?: string;
}

/** Sidebar + header shell used by the role dashboards. */
export function DashboardLayout({ links, title }: DashboardLayoutProps) {
  return (
    <div className="flex min-h-screen bg-cream">
      <Sidebar links={links} title={title} />
      <div className="flex min-w-0 flex-1 flex-col">
        <Header />
        <main className="mx-auto w-full max-w-6xl flex-1 px-4 py-6 sm:px-6 lg:px-8">
          <Outlet />
        </main>
        <MobileNavigation links={links.slice(0, 5)} />
      </div>
    </div>
  );
}

export default DashboardLayout;
