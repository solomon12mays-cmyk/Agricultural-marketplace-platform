package com.agricultural.marketplace.listing.state;

import java.util.Map;
import java.util.Set;

import com.agricultural.marketplace.shared.exception.BusinessRuleException;

/**
 * State machine governing listing lifecycle.
 *
 * <p>Typical flow: {@code DRAFT -> PENDING_REVIEW -> ACTIVE -> SOLD_OUT |
 * EXPIRED}, with {@code PAUSED} and {@code REJECTED} side transitions.</p>
 */
public final class ListingStateMachine {

    public enum Status {
        DRAFT, PENDING_REVIEW, ACTIVE, PAUSED, SOLD_OUT, EXPIRED, REJECTED
    }

    private static final Map<Status, Set<Status>> TRANSITIONS = Map.of(
            Status.DRAFT, Set.of(Status.PENDING_REVIEW, Status.ACTIVE),
            Status.PENDING_REVIEW, Set.of(Status.ACTIVE, Status.REJECTED),
            Status.ACTIVE, Set.of(Status.PAUSED, Status.SOLD_OUT, Status.EXPIRED),
            Status.PAUSED, Set.of(Status.ACTIVE, Status.EXPIRED),
            Status.REJECTED, Set.of(Status.DRAFT),
            Status.SOLD_OUT, Set.of(Status.ACTIVE),
            Status.EXPIRED, Set.of(Status.ACTIVE, Status.DRAFT)
    );

    private ListingStateMachine() {
    }

    public static void assertTransition(Status from, Status to) {
        Set<Status> allowed = allowedTransitions(from);
        if (!allowed.contains(to)) {
            throw new BusinessRuleException(
                    "Illegal listing state transition from " + from + " to " + to);
        }
    }

    public static Set<Status> allowedTransitions(Status from) {
        if (from == null) {
            return Set.of(Status.DRAFT, Status.ACTIVE);
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
