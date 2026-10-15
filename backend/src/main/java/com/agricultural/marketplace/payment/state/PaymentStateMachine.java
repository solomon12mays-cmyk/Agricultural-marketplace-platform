package com.agricultural.marketplace.payment.state;

import java.util.Map;
import java.util.Set;

import com.agricultural.marketplace.shared.exception.BusinessRuleException;

/**
 * State machine governing payment lifecycle.
 *
 * <p>Typical flow: {@code PENDING -> AUTHORIZED -> CAPTURED}, with
 * {@code FAILED} and {@code REFUNDED} alternatives.</p>
 */
public final class PaymentStateMachine {

    public enum Status {
        PENDING, AUTHORIZED, CAPTURED, FAILED, REFUNDED, PARTIALLY_REFUNDED
    }

    private static final Map<Status, Set<Status>> TRANSITIONS = Map.of(
            Status.PENDING, Set.of(Status.AUTHORIZED, Status.CAPTURED, Status.FAILED),
            Status.AUTHORIZED, Set.of(Status.CAPTURED, Status.FAILED),
            Status.CAPTURED, Set.of(Status.REFUNDED, Status.PARTIALLY_REFUNDED),
            Status.PARTIALLY_REFUNDED, Set.of(Status.REFUNDED),
            Status.FAILED, Set.of(),
            Status.REFUNDED, Set.of()
    );

    private PaymentStateMachine() {
    }

    public static void assertTransition(Status from, Status to) {
        Set<Status> allowed = allowedTransitions(from);
        if (!allowed.contains(to)) {
            throw new BusinessRuleException(
                    "Illegal payment state transition from " + from + " to " + to);
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
