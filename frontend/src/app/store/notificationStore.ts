import { create } from "zustand";

export interface AppNotification {
  id: string;
  title: string;
  body?: string;
  channel?: "IN_APP" | "SMS" | "USSD" | "PUSH";
  read: boolean;
  createdAt: string;
}

interface NotificationState {
  notifications: AppNotification[];
  unreadCount: number;
  push: (notification: AppNotification) => void;
  markAsRead: (id: string) => void;
  markAllAsRead: () => void;
  clear: () => void;
}

export const useNotificationStore = create<NotificationState>((set) => ({
  notifications: [],
  unreadCount: 0,

  push: (notification) =>
    set((state) => ({
      notifications: [notification, ...state.notifications],
      unreadCount: state.unreadCount + (notification.read ? 0 : 1),
    })),

  markAsRead: (id) =>
    set((state) => {
      const target = state.notifications.find((item) => item.id === id);
      if (!target || target.read) {
        return state;
      }
      return {
        notifications: state.notifications.map((item) =>
          item.id === id ? { ...item, read: true } : item,
        ),
        unreadCount: Math.max(0, state.unreadCount - 1),
      };
    }),

  markAllAsRead: () =>
    set((state) => ({
      notifications: state.notifications.map((item) => ({ ...item, read: true })),
      unreadCount: 0,
    })),

  clear: () => set({ notifications: [], unreadCount: 0 }),
}));
