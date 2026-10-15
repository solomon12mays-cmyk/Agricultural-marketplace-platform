/** Formats a number with locale-aware grouping, e.g. `1,250.5`. */
export function formatNumber(value: number, fractionDigits = 0): string {
  return new Intl.NumberFormat("en-US", {
    minimumFractionDigits: fractionDigits,
    maximumFractionDigits: fractionDigits,
  }).format(value);
}

/** Formats a quantity with its unit, e.g. `180 kg`. */
export function formatQuantity(value: number, unit: string): string {
  return `${formatNumber(value, value % 1 === 0 ? 0 : 2)} ${unit}`;
}

export default formatNumber;
