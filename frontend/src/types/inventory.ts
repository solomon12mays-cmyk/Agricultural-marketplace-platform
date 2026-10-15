/** Inventory position for an owner/product pair. */
export interface InventoryAccount {
  id: string;
  ownerType: "FARMER" | "ORGANIZATION";
  ownerId: string;
  productId: string;
  productName?: string;
  quantity: number;
  reservedQuantity: number;
  availableQuantity?: number;
  unit: string;
  updatedAt?: string;
}

/** Movement in the inventory history. */
export interface InventoryTransaction {
  id: string;
  accountId: string;
  type: "IN" | "OUT" | "RESERVE" | "RELEASE" | "ADJUST";
  quantity: number;
  referenceType?: string;
  referenceId?: string;
  balanceAfter: number;
  createdAt: string;
}
