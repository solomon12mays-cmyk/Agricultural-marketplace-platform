/** Shared validation helpers used outside zod forms (live checks, params). */

export function isNonEmpty(value: string | undefined | null): boolean {
  return Boolean(value && value.trim().length > 0);
}

export function isValidEmail(value: string): boolean {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value.trim());
}

/** Accepts local or international Ethiopian phone numbers. */
export function isValidPhone(value: string): boolean {
  const digits = value.replace(/\D/g, "");
  return /^(?:251|0)?[1-9]\d{8}$/.test(digits);
}

export function isValidPassword(value: string): boolean {
  return value.length >= 10 && value.length <= 72;
}

export function isPositiveNumber(value: string | number): boolean {
  const parsed = typeof value === "number" ? value : Number(value);
  return Number.isFinite(parsed) && parsed > 0;
}

/** Returns the first failing field message, or null when valid. */
export function firstError(
  checks: Array<[condition: boolean, message: string]>,
): string | null {
  for (const [condition, message] of checks) {
    if (!condition) {
      return message;
    }
  }
  return null;
}
