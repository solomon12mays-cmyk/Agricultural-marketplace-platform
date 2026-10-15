import { z } from "zod";

/** Driver profile schema. */
export const driverSchema = z.object({
  fullName: z.string().min(2, "Enter the driver's full name").max(128),
  licenseNumber: z.string().min(4, "Enter a licence number").max(64),
  phone: z.string().min(9, "Enter a valid phone number").max(32),
  licenseExpiresAt: z.string().optional(),
});

/** Vehicle registration schema. */
export const vehicleSchema = z.object({
  plateNumber: z.string().min(2, "Enter the plate number").max(32),
  vehicleType: z.string().min(2, "Select a vehicle type").max(64),
  capacityKg: z.number().positive("Capacity must be positive").optional(),
});

export type DriverValues = z.infer<typeof driverSchema>;
export type VehicleValues = z.infer<typeof vehicleSchema>;
