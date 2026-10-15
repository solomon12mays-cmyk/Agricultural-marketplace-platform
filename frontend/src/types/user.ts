/** Account profile shared by every role. */
export interface UserProfile {
  id: string;
  fullName: string;
  phone: string;
  email?: string;
  region?: string;
  avatarUrl?: string;
  status: "ACTIVE" | "SUSPENDED" | "PENDING";
  createdAt: string;
}

/** Operator/admin view of a platform user. */
export interface AdminUser {
  id: string;
  username: string;
  roles: string[];
  status: string;
  lastLoginAt?: string;
  createdAt: string;
}
