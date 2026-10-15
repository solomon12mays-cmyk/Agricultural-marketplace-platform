import type { ReactNode } from "react";
import { BrowserRouter } from "react-router-dom";
import { Toast } from "../components/feedback/Toast";

/**
 * Global providers: router, feedback surfaces and any future contexts
 * (theming, query clients, ...).
 */
export default function AppProviders({ children }: { children: ReactNode }) {
  return (
    <BrowserRouter>
      {children}
      <Toast />
    </BrowserRouter>
  );
}
