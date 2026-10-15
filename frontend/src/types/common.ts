export type AsyncStatus = "idle" | "loading" | "success" | "error";

export type Maybe<T> = T | null | undefined;

export interface SelectOption {
  value: string;
  label: string;
}

export interface Identifiable {
  id: string | number;
}
