import { z } from "zod";

/** Farmer profile schema. */
export const farmerSchema = z.object({
  fullName: z.string().min(2, "Enter the farmer's full name").max(128),
  nationalId: z.string().min(4, "Enter a national ID / FAN number").max(64),
  phone: z.string().min(9, "Enter a valid phone number").max(32),
  region: z.string().max(64).optional(),
  zone: z.string().max(64).optional(),
  woreda: z.string().max(64).optional(),
});

export type FarmerValues = z.infer<typeof farmerSchema>;
