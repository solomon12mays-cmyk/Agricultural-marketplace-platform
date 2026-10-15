import { z } from "zod";

/** Organization registration schema. */
export const organizationSchema = z.object({
  name: z.string().min(2, "Enter the organization name").max(128),
  taxId: z.string().max(64).optional(),
  registrationNumber: z.string().max(64).optional(),
  phone: z.string().min(9, "Enter a valid phone number").max(32),
  email: z.string().email("Enter a valid email address").optional().or(z.literal("")),
});

export type OrganizationValues = z.infer<typeof organizationSchema>;
