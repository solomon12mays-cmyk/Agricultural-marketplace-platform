package com.agricultural.marketplace.order.state;

import java.util.Map;
import java.util.Set;

import com.agricultural.marketplace.shared.exception.BusinessRuleException;

/**
 * State machine governing order lifecycle.
 *
 * <p>Typical flow: {@code PENDING -> CONFIRMED -> PAID -> PICKING ->
 * IN_TRANSIT -> DELIVERED -> COMPLETED}, with {@code CANCELLED} and
 * {@code REFUNDED} terminal alternatives.</p>
 */
public final class OrderStateMachine {

    public enum Status {
        PENDING, CONFIRMED, PAID, PICKING, IN_TRANSIT, DELIVERED, COMPLETED, CANCELLED, REFUNDED
    }

    private static final Map<Status, Set<Status>> TRANSITIONS = Map.of(
            Status.PENDING, Set.of(Status.CONFIRMED, Status.CANCELLED),
            Status.CONFIRMED, Set.of(Status.PAID, Status.CANCELLED),
            Status.PAID, Set.of(Status.PICKING, Status.CANCELLED, Status.REFUNDED),
            Status.PICKING, Set.of(Status.IN_TRANSIT, Status.CANCELLED),
            Status.IN_TRANSIT, Set.of(Status.DELIVERED),
            Status.DELIVERED, Set.of(Status.COMPLETED, Status.REFUNDED),
            Status.COMPLETED, Set.of(Status.REFUNDED),
            Status.CANCELLED, Set.of(),
            Status.REFUNDED, Set.of()
    );

    private OrderStateMachine() {
    }

    public static void assertTransition(Status from, Status to) {
        Set<Status> allowed = allowedTransitions(from);
        if (!allowed.contains(to)) {
            throw new BusinessRuleException(
                    "Illegal order state transition from " + from + " to " + to);
        }
    }

    public static Set<Status> allowedTransitions(Status from) {
        if (from == null) {
            return Set.of(Status.PENDING);
        }
        return TRANSITIONS.getOrDefault(from, Set.of());
    }

    public static boolean canTransition(Status from, Status to) {
        try {
            assertTransition(from, to);
            return true;
        } catch (BusinessRuleException ex) {
            return false;
        }
    }
}
