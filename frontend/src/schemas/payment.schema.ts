import { z } from "zod";

/** Payment initiation schema. */
export const initiatePaymentSchema = z.object({
  method: z.enum(["TELEBIRR", "CARD", "BANK_TRANSFER", "CASH_ON_DELIVERY"], {
    message: "Select a payment method",
  }),
  acceptTerms: z.literal(true, {
    message: "Confirm the payment terms to continue",
  }),
});

export type InitiatePaymentValues = z.infer<typeof initiatePaymentSchema>;
