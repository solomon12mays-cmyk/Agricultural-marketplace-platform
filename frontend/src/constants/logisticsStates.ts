/** Logistics assignment / trip states. */
export const LOGISTICS_STATES = [
  "PENDING",
  "ASSIGNED",
  "PICKING_UP",
  "IN_TRANSIT",
  "DELIVERED",
  "CANCELLED",
  "FAILED",
] as const;

export type LogisticsState = (typeof LOGISTICS_STATES)[number];

export const LOGISTICS_STATE_LABELS: Record<LogisticsState, string> = {
  PENDING: "Pending assignment",
  ASSIGNED: "Driver assigned",
  PICKING_UP: "Pickup in progress",
  IN_TRANSIT: "In transit",
  DELIVERED: "Delivered",
  CANCELLED: "Cancelled",
  FAILED: "Failed",
};
