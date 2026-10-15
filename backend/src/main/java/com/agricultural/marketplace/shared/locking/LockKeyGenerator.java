package com.agricultural.marketplace.shared.locking;

/**
 * Builds consistent, namespaced Redis keys for distributed locks.
 */
public final class LockKeyGenerator {

    public static final String PREFIX = "agri:lock:";

    private LockKeyGenerator() {
    }

    public static String of(Object... parts) {
        StringBuilder builder = new StringBuilder(PREFIX);
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                builder.append(':');
            }
            builder.append(parts[i]);
        }
        return builder.toString();
    }
}
