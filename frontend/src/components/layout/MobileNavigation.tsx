import { NavLink } from "react-router-dom";

interface MobileNavigationProps {
  links: Array<{ to: string; label: string }>;
}

/** Bottom navigation shown on small screens inside dashboards. */
export function MobileNavigation({ links }: MobileNavigationProps) {
  return (
    <nav
      aria-label="Mobile navigation"
      className="sticky bottom-0 z-30 flex border-t border-slate-200 bg-white lg:hidden"
    >
      {links.map((link) => (
        <NavLink
          key={link.to}
          to={link.to}
          end={link.to === "/"}
          className={({ isActive }) =>
            `flex-1 px-2 py-3 text-center text-xs font-medium ${
              isActive ? "text-brand-deep" : "text-slate-500"
            }`
          }
        >
          {link.label}
        </NavLink>
      ))}
    </nav>
  );
}

export default MobileNavigation;
