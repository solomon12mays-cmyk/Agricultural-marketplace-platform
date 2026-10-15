/** Listing lifecycle states, mirroring the backend state machine. */
export const LISTING_STATES = [
  "DRAFT",
  "PENDING_REVIEW",
  "ACTIVE",
  "PAUSED",
  "SOLD_OUT",
  "EXPIRED",
  "REJECTED",
] as const;

export type ListingState = (typeof LISTING_STATES)[number];

export const LISTING_STATE_LABELS: Record<ListingState, string> = {
  DRAFT: "Draft",
  PENDING_REVIEW: "Pending review",
  ACTIVE: "Active",
  PAUSED: "Paused",
  SOLD_OUT: "Sold out",
  EXPIRED: "Expired",
  REJECTED: "Rejected",
};
