import { isAxiosError, type AxiosError } from "axios";
import type { ApiErrorShape } from "../types/api";

/** Normalizes any thrown value into a user-facing message. */
export function toErrorMessage(error: unknown): string {
  if (isAxiosError(error)) {
    const payload = extractApiError(error);
    if (payload?.message) {
      return payload.message;
    }
    if (error.response) {
      return `Request failed (${error.response.status}).`;
    }
    return "The server could not be reached. Please try again.";
  }
  if (error instanceof Error) {
    return error.message;
  }
  return "An unexpected error occurred.";
}

/** Extracts the backend's `ApiError` payload when present. */
export function extractApiError(error: AxiosError): ApiErrorShape | null {
  const data = error.response?.data as ApiErrorShape | undefined;
  if (data && typeof data === "object" && "message" in data) {
    return data;
  }
  return null;
}
