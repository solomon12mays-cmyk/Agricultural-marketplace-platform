import { Link } from "react-router-dom";
import { appConfig } from "../../app/config/app.config";
import { useUiStore } from "../../app/store/uiStore";

/** Top application bar with brand, search slot and actions. */
export function Header() {
  const toggleSidebar = useUiStore((state) => state.toggleSidebar);

  return (
    <header className="sticky top-0 z-40 border-b border-slate-200 bg-white/90 backdrop-blur">
      <div className="mx-auto flex h-16 w-full max-w-7xl items-center justify-between gap-4 px-4 sm:px-6 lg:px-8">
        <div className="flex items-center gap-3">
          <button
            type="button"
            aria-label="Toggle navigation"
            onClick={toggleSidebar}
            className="rounded-lg p-2 text-slate-500 hover:bg-slate-100 lg:hidden"
          >
            ☰
          </button>
          <Link to="/" className="flex items-center gap-2 font-display text-lg font-bold text-brand-deep">
            <img src="/logo.svg" alt="" aria-hidden="true" className="h-8 w-8" />
            {appConfig.name}
          </Link>
        </div>
        <div className="flex items-center gap-2">
          {/* TODO: global search, notifications bell and account menu. */}
        </div>
      </div>
    </header>
  );
}

export default Header;
