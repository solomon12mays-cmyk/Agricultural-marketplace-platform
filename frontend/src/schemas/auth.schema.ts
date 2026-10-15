import { z } from "zod";

/** Sign-in form schema. */
export const loginSchema = z.object({
  username: z.string().min(3, "Enter your username or phone number"),
  password: z.string().min(10, "Password must be at least 10 characters").max(72),
});

/** Registration form schema. */
export const registerSchema = z
  .object({
    username: z.string().min(3).max(64),
    email: z.string().email("Enter a valid email address"),
    phone: z.string().min(9, "Enter a valid phone number"),
    password: z.string().min(10, "Password must be at least 10 characters").max(72),
    confirmPassword: z.string(),
    fullName: z.string().min(2, "Enter your full name").max(128),
  })
  .refine((values) => values.password === values.confirmPassword, {
    message: "Passwords do not match",
    path: ["confirmPassword"],
  });

/** Password change schema. */
export const changePasswordSchema = z
  .object({
    currentPassword: z.string().min(1),
    newPassword: z.string().min(10).max(72),
    confirmPassword: z.string(),
  })
  .refine((values) => values.newPassword === values.confirmPassword, {
    message: "Passwords do not match",
    path: ["confirmPassword"],
  });

/** Password recovery request schema. */
export const forgotPasswordSchema = z.object({
  identifier: z.string().min(3, "Enter the phone number or email on your account"),
});

export type LoginValues = z.infer<typeof loginSchema>;
export type RegisterValues = z.infer<typeof registerSchema>;
export type ChangePasswordValues = z.infer<typeof changePasswordSchema>;
export type ForgotPasswordValues = z.infer<typeof forgotPasswordSchema>;
