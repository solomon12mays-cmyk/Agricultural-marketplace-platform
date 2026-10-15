import { appConfig } from "../../app/config/app.config";

/** Site footer shown on public pages. */
export function Footer() {
  return (
    <footer className="border-t border-slate-200 bg-white">
      <div className="mx-auto flex w-full max-w-7xl flex-col gap-2 px-4 py-8 text-sm text-slate-500 sm:flex-row sm:items-center sm:justify-between sm:px-6 lg:px-8">
        <p>© {new Date().getFullYear()} {appConfig.name}. All rights reserved.</p>
        <nav aria-label="Footer" className="flex gap-4">
          {/* TODO: help center, terms and privacy links. */}
          <span>Help</span>
          <span>Terms</span>
          <span>Privacy</span>
        </nav>
      </div>
    </footer>
  );
}

export default Footer;
