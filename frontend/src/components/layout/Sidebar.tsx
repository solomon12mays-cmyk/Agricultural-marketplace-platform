import type { ReactNode } from "react";
import { NavLink } from "react-router-dom";

export interface SidebarLink {
  to: string;
  label: string;
}

interface SidebarProps {
  title?: string;
  links: SidebarLink[];
  footer?: ReactNode;
}

/** Primary sidebar navigation for dashboard layouts. */
export function Sidebar({ title = "Menu", links, footer }: SidebarProps) {
  return (
    <aside className="hidden w-60 shrink-0 border-r border-slate-200 bg-white lg:block">
      <nav aria-label={title} className="flex flex-col gap-1 p-4">
        <p className="mb-2 px-3 text-xs font-semibold uppercase tracking-wide text-slate-400">{title}</p>
        {links.map((link) => (
          <NavLink
            key={link.to}
            to={link.to}
            end={link.to === "/"}
            className={({ isActive }) =>
              `rounded-lg px-3 py-2 text-sm font-medium transition ${
                isActive ? "bg-brand-deep text-white" : "text-slate-600 hover:bg-slate-100 hover:text-brand-deep"
              }`
            }
          >
            {link.label}
          </NavLink>
        ))}
      </nav>
      {footer ? <div className="border-t border-slate-200 p-4">{footer}</div> : null}
    </aside>
  );
}

export default Sidebar;
