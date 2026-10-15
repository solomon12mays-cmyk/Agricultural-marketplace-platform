import type { ReactNode } from "react";

interface CardProps {
  title?: string;
  actions?: ReactNode;
  children: ReactNode;
  className?: string;
}

/** Surface container used across dashboards and detail pages. */
export function Card({ title, actions, children, className = "" }: CardProps) {
  return (
    <section className={`rounded-xl border border-slate-200 bg-white p-5 shadow-sm ${className}`}>
      {(title ?? actions) && (
        <header className="mb-4 flex items-center justify-between gap-3">
          {title ? <h3 className="text-base font-semibold text-brand-deep">{title}</h3> : <span />}
          {actions}
        </header>
      )}
      {children}
    </section>
  );
}

export default Card;
