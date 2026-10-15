/** Order line item. */
export interface OrderItem {
  id: string;
  listingId: string;
  productId: string;
  productName: string;
  quantity: number;
  unit: string;
  unitPrice: number;
  lineTotal: number;
}

/** Order aggregate. */
export interface Order {
  id: string;
  orderNumber: string;
  buyerType: "BUYER" | "ORGANIZATION";
  buyerId: string;
  status: string;
  paymentStatus: string;
  totalAmount: number;
  currency: string;
  deliveryRegion?: string;
  deliveryAddress?: string;
  notes?: string;
  placedAt: string;
  items?: OrderItem[];
}
