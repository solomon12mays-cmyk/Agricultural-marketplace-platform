package com.agricultural.marketplace.organization.state;

import com.agricultural.marketplace.shared.exception.BusinessRuleException;
import java.util.Map;
import java.util.Set;

/**
 * State machine governing organization verification.
 *
 * <p>Organizations are submitted as {@code PENDING_VERIFICATION}, enter
 * {@code UNDER_REVIEW}, and are either {@code VERIFIED} or {@code REJECTED}.
 * Verified organizations become {@code ACTIVE} after the required password
 * change. Active and verified organizations may be suspended.</p>
 */
public final class OrganizationVerificationStateMachine {

    public enum Status {
        PENDING_VERIFICATION, UNDER_REVIEW, VERIFIED, REJECTED, SUSPENDED, ACTIVE
    }

    private OrganizationVerificationStateMachine() {
    }

    private static final Map<Status, Set<Status>> TRANSITIONS = Map.of(
            Status.PENDING_VERIFICATION, Set.of(Status.UNDER_REVIEW, Status.REJECTED),
            Status.UNDER_REVIEW, Set.of(Status.VERIFIED, Status.REJECTED),
            Status.VERIFIED, Set.of(Status.ACTIVE, Status.SUSPENDED),
            Status.REJECTED, Set.of(Status.PENDING_VERIFICATION),
            Status.SUSPENDED, Set.of(Status.ACTIVE),
            Status.ACTIVE, Set.of(Status.SUSPENDED));

    public static void assertTransition(Status from, Status to) {
        if (from == null || to == null || !allowedTransitions(from).contains(to)) {
            throw new BusinessRuleException("Organization verification cannot transition from " + from + " to " + to);
        }
    }

    public static Set<Status> allowedTransitions(Status from) {
        return TRANSITIONS.getOrDefault(from, Set.of());
    }

    public static boolean canTransition(Status from, Status to) {
        try {
            assertTransition(from, to);
            return true;
        } catch (UnsupportedOperationException | BusinessRuleException ex) {
            return false;
        }
    }
}
