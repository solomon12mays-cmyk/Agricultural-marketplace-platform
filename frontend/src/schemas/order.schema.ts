import { z } from "zod";

/** Checkout / order creation schema. */
export const createOrderSchema = z.object({
  deliveryRegion: z.string().min(2, "Select a delivery region").max(64),
  deliveryAddress: z.string().min(5, "Enter a delivery address").max(255),
  notes: z.string().max(500).optional(),
});

export type CreateOrderValues = z.infer<typeof createOrderSchema>;
