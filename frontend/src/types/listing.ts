/** Marketplace listing. */
export interface Listing {
  id: string;
  sellerType: "FARMER" | "ORGANIZATION";
  sellerId: string;
  sellerName?: string;
  productId: string;
  productName: string;
  quantityAvailable: number;
  unit: string;
  pricePerUnit: number;
  currency: string;
  status: string;
  region?: string;
  startsAt?: string;
  endsAt?: string;
  createdAt: string;
}
