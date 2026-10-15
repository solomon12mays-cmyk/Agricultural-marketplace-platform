/** Order lifecycle states, mirroring the backend state machine. */
export const ORDER_STATES = [
  "PENDING",
  "CONFIRMED",
  "PAID",
  "PICKING",
  "IN_TRANSIT",
  "DELIVERED",
  "COMPLETED",
  "CANCELLED",
  "REFUNDED",
] as const;

export type OrderState = (typeof ORDER_STATES)[number];

export const ORDER_STATE_LABELS: Record<OrderState, string> = {
  PENDING: "Pending",
  CONFIRMED: "Confirmed",
  PAID: "Paid",
  PICKING: "Picking up",
  IN_TRANSIT: "In transit",
  DELIVERED: "Delivered",
  COMPLETED: "Completed",
  CANCELLED: "Cancelled",
  REFUNDED: "Refunded",
};
