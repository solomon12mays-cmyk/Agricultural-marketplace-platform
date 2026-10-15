import { z } from "zod";

/** Farm registration/edit schema. */
export const farmSchema = z.object({
  name: z.string().min(2, "Enter a farm name").max(128),
  farmType: z.string().max(64).optional(),
  sizeHectares: z.number().positive("Size must be greater than zero").optional(),
  region: z.string().max(64).optional(),
  zone: z.string().max(64).optional(),
  woreda: z.string().max(64).optional(),
  village: z.string().max(128).optional(),
  latitude: z.number().min(-90).max(90).optional(),
  longitude: z.number().min(-180).max(180).optional(),
});

export type FarmValues = z.infer<typeof farmSchema>;
