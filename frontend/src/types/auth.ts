/** Roles a platform account can hold. */
export type Role =
  | "FARMER"
  | "ORGANIZATION"
  | "BUYER"
  | "DRIVER"
  | "OPERATOR"
  | "ADMIN";

export interface AuthUser {
  id: string;
  username: string;
  displayName?: string;
  phone?: string;
  email?: string;
  roles: Role[];
  permissions: string[];
  mustChangePassword?: boolean;
}

export interface LoginCredentials {
  username: string;
  password: string;
}

export interface RegisterPayload {
  username: string;
  email: string;
  phone: string;
  password: string;
  fullName: string;
}

export interface TokenPair {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
  tokenType?: string;
}

export interface AuthSession {
  user: AuthUser;
  tokens: TokenPair;
}
