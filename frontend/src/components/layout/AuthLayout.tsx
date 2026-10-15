import { Link } from "react-router-dom";
import { appConfig } from "../../app/config/app.config";

/** Centered shell for sign-in, registration and recovery screens. */
export function AuthLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-brand-deep px-4 py-10">
      <Link to="/" className="mb-8 flex items-center gap-3 text-white">
        <img src="/logo.svg" alt="" aria-hidden="true" className="h-10 w-10" />
        <span className="font-display text-xl font-bold">{appConfig.name}</span>
      </Link>
      <div className="w-full max-w-md rounded-2xl bg-white p-6 shadow-xl sm:p-8">{children}</div>
    </div>
  );
}

export default AuthLayout;
