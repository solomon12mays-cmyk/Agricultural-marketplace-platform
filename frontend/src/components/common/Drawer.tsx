import { useEffect, type ReactNode } from "react";

interface DrawerProps {
  open: boolean;
  title: string;
  onClose: () => void;
  children: ReactNode;
  side?: "left" | "right";
}

/** Slide-in panel used for filters, detail panels and mobile menus. */
export function Drawer({ open, title, onClose, children, side = "right" }: DrawerProps) {
  useEffect(() => {
    if (!open) {
      return;
    }
    const onKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape") {
        onClose();
      }
    };
    window.addEventListener("keydown", onKeyDown);
    return () => window.removeEventListener("keydown", onKeyDown);
  }, [open, onClose]);

  if (!open) {
    return null;
  }

  return (
    <div className="fixed inset-0 z-50 bg-brand-ink/40" role="presentation" onClick={onClose}>
      <aside
        role="dialog"
        aria-modal="true"
        aria-label={title}
        className={`absolute top-0 ${side === "right" ? "right-0" : "left-0"} h-full w-full max-w-sm bg-white p-6 shadow-xl`}
        onClick={(event) => event.stopPropagation()}
      >
        <div className="flex items-start justify-between gap-4">
          <h2 className="text-lg font-semibold text-brand-deep">{title}</h2>
          <button
            type="button"
            aria-label="Close panel"
            onClick={onClose}
            className="rounded-lg p-1 text-slate-400 hover:bg-slate-100 hover:text-slate-700"
          >
            ✕
          </button>
        </div>
        <div className="mt-4 text-sm text-slate-600">{children}</div>
      </aside>
    </div>
  );
}

export default Drawer;
