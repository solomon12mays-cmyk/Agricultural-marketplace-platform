/**
 * Normalises Ethiopian phone numbers to `+251 9XX XXX XXX` format.
 * Returns the input unchanged when it does not look like a local number.
 */
export function formatPhone(value: string): string {
  const digits = value.replace(/\D/g, "");
  let national: string | null = null;

  if (digits.startsWith("251") && digits.length >= 12) {
    national = digits.slice(3);
  } else if (digits.startsWith("0") && digits.length === 10) {
    national = digits.slice(1);
  } else if (digits.length === 9) {
    national = digits;
  }

  if (!national || !/^[1-9]\d{8}$/.test(national)) {
    return value;
  }

  return `+251 ${national.slice(0, 3)} ${national.slice(3, 6)} ${national.slice(6)}`;
}

export default formatPhone;
