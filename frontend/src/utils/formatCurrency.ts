import { appConfig } from "../app/config/app.config";

/** Formats an amount as currency, e.g. `ETB 4,200.00`. */
export function formatCurrency(amount: number, currency: string = appConfig.defaultCurrency): string {
  return new Intl.NumberFormat(appConfig.locale, {
    style: "currency",
    currency,
    minimumFractionDigits: 2,
  }).format(amount);
}

export default formatCurrency;
