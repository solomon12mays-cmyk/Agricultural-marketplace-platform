const PREFIX = "agri:";

/** Reads and parses a JSON value from local storage, never throwing. */
export function readJson<T>(key: string): T | null {
  try {
    const raw = window.localStorage.getItem(PREFIX + key);
    return raw ? (JSON.parse(raw) as T) : null;
  } catch {
    return null;
  }
}

/** Serializes a JSON value into local storage, never throwing. */
export function writeJson(key: string, value: unknown): void {
  try {
    window.localStorage.setItem(PREFIX + key, JSON.stringify(value));
  } catch {
    // Storage may be unavailable (private mode, quota); ignore.
  }
}

/** Removes a value from local storage, never throwing. */
export function remove(key: string): void {
  try {
    window.localStorage.removeItem(PREFIX + key);
  } catch {
    // Ignore unavailable storage.
  }
}
