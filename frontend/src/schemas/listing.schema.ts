import { z } from "zod";

/** Listing create/edit schema. */
export const listingSchema = z
  .object({
    productId: z.string().min(1, "Select a product"),
    quantityAvailable: z.number().positive("Quantity must be greater than zero"),
    unit: z.string().min(1, "Select a unit"),
    pricePerUnit: z.number().positive("Price must be greater than zero"),
    currency: z.string().length(3, "Use a 3-letter currency code"),
    region: z.string().max(64).optional(),
    startsAt: z.string().optional(),
    endsAt: z.string().optional(),
  })
  .refine((values) => !values.startsAt || !values.endsAt || values.startsAt < values.endsAt, {
    message: "The end date must be after the start date",
    path: ["endsAt"],
  });

export type ListingValues = z.infer<typeof listingSchema>;
