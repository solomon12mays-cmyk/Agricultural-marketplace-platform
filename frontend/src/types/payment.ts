/** Payment aggregate. */
export interface Payment {
  id: string;
  orderId: string;
  payerType: string;
  payerId: string;
  amount: number;
  currency: string;
  method?: string;
  status: string;
  gateway?: string;
  gatewayReference?: string;
  createdAt: string;
  paidAt?: string;
}

/** A fee line shown in the checkout breakdown. */
export interface FeeLine {
  code: string;
  label: string;
  amount: number;
  currency: string;
}
