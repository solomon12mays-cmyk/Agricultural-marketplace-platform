import { appConfig } from "../app/config/app.config";

/** Formats an ISO date string as a medium date, e.g. `8 Oct 2026`. */
export function formatDate(value: string | Date | undefined): string {
  if (!value) {
    return "—";
  }
  const date = typeof value === "string" ? new Date(value) : value;
  if (Number.isNaN(date.getTime())) {
    return "—";
  }
  return new Intl.DateTimeFormat(appConfig.locale, {
    day: "numeric",
    month: "short",
    year: "numeric",
  }).format(date);
}

/** Formats an ISO date-time string with time, e.g. `8 Oct 2026, 14:30`. */
export function formatDateTime(value: string | Date | undefined): string {
  if (!value) {
    return "—";
  }
  const date = typeof value === "string" ? new Date(value) : value;
  if (Number.isNaN(date.getTime())) {
    return "—";
  }
  return new Intl.DateTimeFormat(appConfig.locale, {
    day: "numeric",
    month: "short",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  }).format(date);
}

export default formatDate;
