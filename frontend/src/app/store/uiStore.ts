import { create } from "zustand";

export interface ToastMessage {
  id: string;
  kind: "info" | "success" | "warning" | "error";
  text: string;
}

interface UiState {
  toasts: ToastMessage[];
  sidebarOpen: boolean;
  pushToast: (kind: ToastMessage["kind"], text: string) => void;
  dismissToast: (id: string) => void;
  toggleSidebar: () => void;
  closeSidebar: () => void;
}

let toastSequence = 0;

export const useUiStore = create<UiState>((set) => ({
  toasts: [],
  sidebarOpen: false,

  pushToast: (kind, text) => {
    toastSequence += 1;
    const toast: ToastMessage = { id: `toast-${toastSequence}`, kind, text };
    set((state) => ({ toasts: [...state.toasts, toast] }));
    window.setTimeout(() => {
      set((state) => ({ toasts: state.toasts.filter((item) => item.id !== toast.id) }));
    }, 5000);
  },

  dismissToast: (id) =>
    set((state) => ({ toasts: state.toasts.filter((item) => item.id !== id) })),

  toggleSidebar: () => set((state) => ({ sidebarOpen: !state.sidebarOpen })),

  closeSidebar: () => set({ sidebarOpen: false }),
}));
