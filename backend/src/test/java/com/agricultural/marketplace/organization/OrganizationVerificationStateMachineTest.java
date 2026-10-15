package com.agricultural.marketplace.organization;

import com.agricultural.marketplace.organization.state.OrganizationVerificationStateMachine;
import com.agricultural.marketplace.shared.exception.BusinessRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrganizationVerificationStateMachineTest {

    @Test
    @DisplayName("Valid verification workflow should transition correctly")
    void testValidTransitions() {
        assertDoesNotThrow(() -> OrganizationVerificationStateMachine.assertTransition(
                OrganizationVerificationStateMachine.Status.PENDING_VERIFICATION,
                OrganizationVerificationStateMachine.Status.UNDER_REVIEW));

        assertDoesNotThrow(() -> OrganizationVerificationStateMachine.assertTransition(
                OrganizationVerificationStateMachine.Status.UNDER_REVIEW,
                OrganizationVerificationStateMachine.Status.VERIFIED));

        assertDoesNotThrow(() -> OrganizationVerificationStateMachine.assertTransition(
                OrganizationVerificationStateMachine.Status.VERIFIED,
                OrganizationVerificationStateMachine.Status.ACTIVE));
    }

    @Test
    @DisplayName("Invalid transition should throw BusinessRuleException")
    void testInvalidTransition() {
        assertThrows(BusinessRuleException.class, () ->
                OrganizationVerificationStateMachine.assertTransition(
                        OrganizationVerificationStateMachine.Status.PENDING_VERIFICATION,
                        OrganizationVerificationStateMachine.Status.ACTIVE));
    }
}

