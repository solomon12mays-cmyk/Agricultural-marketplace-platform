/** Payment lifecycle states, mirroring the backend state machine. */
export const PAYMENT_STATES = [
  "PENDING",
  "AUTHORIZED",
  "CAPTURED",
  "FAILED",
  "REFUNDED",
  "PARTIALLY_REFUNDED",
] as const;

export type PaymentState = (typeof PAYMENT_STATES)[number];

export const PAYMENT_STATE_LABELS: Record<PaymentState, string> = {
  PENDING: "Pending",
  AUTHORIZED: "Authorized",
  CAPTURED: "Captured",
  FAILED: "Failed",
  REFUNDED: "Refunded",
  PARTIALLY_REFUNDED: "Partially refunded",
};
