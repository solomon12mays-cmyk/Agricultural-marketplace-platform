import { useNotificationStore } from "../../app/store/notificationStore";
import EmptyState from "../common/EmptyState";

/** Dropdown/panel listing the user's in-app notifications. */
export function NotificationPanel() {
  const notifications = useNotificationStore((state) => state.notifications);
  const markAsRead = useNotificationStore((state) => state.markAsRead);
  const markAllAsRead = useNotificationStore((state) => state.markAllAsRead);

  return (
    <div className="w-full max-w-sm rounded-xl border border-slate-200 bg-white p-3 shadow-lg">
      <div className="flex items-center justify-between px-2 py-1">
        <p className="text-sm font-semibold text-brand-deep">Notifications</p>
        <button
          type="button"
          onClick={markAllAsRead}
          className="text-xs font-medium text-brand-green hover:underline"
        >
          Mark all as read
        </button>
      </div>
      {notifications.length === 0 ? (
        <EmptyState title="No notifications yet" description="Updates about your orders and account will appear here." />
      ) : (
        <ul className="mt-2 max-h-80 divide-y divide-slate-100 overflow-y-auto">
          {notifications.map((notification) => (
            <li key={notification.id}>
              <button
                type="button"
                onClick={() => markAsRead(notification.id)}
                className={`w-full rounded-lg px-3 py-2.5 text-left hover:bg-slate-50 ${notification.read ? "opacity-70" : ""}`}
              >
                <p className="text-sm font-medium text-slate-800">{notification.title}</p>
                {notification.body ? <p className="mt-0.5 text-xs text-slate-500">{notification.body}</p> : null}
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default NotificationPanel;
