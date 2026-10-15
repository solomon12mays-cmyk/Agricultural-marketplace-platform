/** Transport assignment shown to operators and drivers. */
export interface LogisticsAssignment {
  id: string;
  orderId: string;
  driverId?: string;
  driverName?: string;
  vehicleId?: string;
  status: string;
  pickupAddress?: string;
  dropoffAddress?: string;
  quotedPrice?: number;
  currency?: string;
  distanceKm?: number;
  assignedAt?: string;
  createdAt: string;
}

/** Candidate driver returned by matching. */
export interface DriverOption {
  driverId: string;
  driverName: string;
  vehicleType: string;
  capacityKg?: number;
  rating?: number;
  distanceKm?: number;
  estimatedPrice?: number;
}
