package com.agricultural.marketplace.payment;

import com.agricultural.marketplace.payment.state.PaymentStateMachine;
import com.agricultural.marketplace.shared.exception.BusinessRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentStateMachineTest {

    @Test
    @DisplayName("Valid payment flow transitions should succeed")
    void testValidTransitions() {
        assertDoesNotThrow(() -> PaymentStateMachine.assertTransition(
                PaymentStateMachine.Status.PENDING, PaymentStateMachine.Status.AUTHORIZED));
        assertDoesNotThrow(() -> PaymentStateMachine.assertTransition(
                PaymentStateMachine.Status.AUTHORIZED, PaymentStateMachine.Status.CAPTURED));
        assertDoesNotThrow(() -> PaymentStateMachine.assertTransition(
                PaymentStateMachine.Status.CAPTURED, PaymentStateMachine.Status.REFUNDED));
    }

    @Test
    @DisplayName("Invalid transition should throw BusinessRuleException")
    void testInvalidTransition() {
        assertThrows(BusinessRuleException.class, () ->
                PaymentStateMachine.assertTransition(
                        PaymentStateMachine.Status.PENDING, PaymentStateMachine.Status.REFUNDED));
    }
}

