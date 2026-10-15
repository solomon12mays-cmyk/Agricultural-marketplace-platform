import { create } from "zustand";
import type { AuthUser, TokenPair } from "../../types/auth";
import { readJson, remove, writeJson } from "../../utils/storage";

const SESSION_KEY = "session";

interface StoredSession {
  user: AuthUser;
  tokens: TokenPair;
}

interface AuthState {
  user: AuthUser | null;
  tokens: TokenPair | null;
  status: "idle" | "restoring" | "authenticated" | "anonymous";
  /** Restores the persisted session on startup. */
  hydrate: () => void;
  setSession: (user: AuthUser, tokens: TokenPair) => void;
  setUser: (user: AuthUser) => void;
  clearAuth: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  tokens: null,
  status: "idle",

  hydrate: () => {
    const stored = readJson<StoredSession>(SESSION_KEY);
    if (stored?.user && stored.tokens) {
      set({ user: stored.user, tokens: stored.tokens, status: "authenticated" });
    } else {
      set({ status: "anonymous" });
    }
  },

  setSession: (user, tokens) => {
    writeJson(SESSION_KEY, { user, tokens });
    set({ user, tokens, status: "authenticated" });
  },

  setUser: (user) => {
    const stored = readJson<StoredSession>(SESSION_KEY);
    if (stored) {
      writeJson(SESSION_KEY, { ...stored, user });
    }
    set({ user });
  },

  clearAuth: () => {
    remove(SESSION_KEY);
    set({ user: null, tokens: null, status: "anonymous" });
  },
}));
