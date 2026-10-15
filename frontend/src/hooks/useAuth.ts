import { authApi } from "../api/endpoints/authApi";
import { useAuthStore } from "../app/store/authStore";
import { toErrorMessage } from "../api/errorHandler";
import type { AuthUser, LoginCredentials, RegisterPayload, TokenPair } from "../types/auth";
import { useUiStore } from "../app/store/uiStore";

/**
 * Auth actions bound to the auth store.
 *
 * TODO: swap the placeholders for real `authApi` calls once the backend
 * endpoints are implemented (the shapes already match the API contracts).
 */
export function useAuth() {
  const user = useAuthStore((state) => state.user);
  const status = useAuthStore((state) => state.status);
  const setSession = useAuthStore((state) => state.setSession);
  const clearAuth = useAuthStore((state) => state.clearAuth);
  const pushToast = useUiStore((state) => state.pushToast);

  async function login(credentials: LoginCredentials): Promise<boolean> {
    try {
      // TODO: const { user, tokens } = await authApi.login(credentials);
      void authApi;
      void credentials;
      throw new Error("Sign-in is not available yet.");
    } catch (error) {
      pushToast("error", toErrorMessage(error));
      return false;
    }
  }

  async function register(payload: RegisterPayload): Promise<boolean> {
    try {
      // TODO: await authApi.register(payload);
      void payload;
      throw new Error("Registration is not available yet.");
    } catch (error) {
      pushToast("error", toErrorMessage(error));
      return false;
    }
  }

  function applySession(nextUser: AuthUser, tokens: TokenPair) {
    setSession(nextUser, tokens);
  }

  function logout() {
    clearAuth();
    // TODO: call authApi.logout() to revoke the refresh token server-side.
  }

  return {
    user,
    status,
    isAuthenticated: user !== null,
    login,
    register,
    applySession,
    logout,
  };
}

export default useAuth;
