import type { ReactNode } from "react";
import Header from "./Header";

interface AppLayoutProps {
  children: ReactNode;
}

/** Minimal shell: header + main region. Used for cross-role screens. */
export function AppLayout({ children }: AppLayoutProps) {
  return (
    <div className="flex min-h-screen flex-col bg-cream">
      <Header />
      <main className="mx-auto w-full max-w-7xl flex-1 px-4 py-6 sm:px-6 lg:px-8">{children}</main>
    </div>
  );
}

export default AppLayout;
