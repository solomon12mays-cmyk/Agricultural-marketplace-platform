package com.agricultural.marketplace.order;

import com.agricultural.marketplace.order.state.OrderStateMachine;
import com.agricultural.marketplace.shared.exception.BusinessRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderStateMachineTest {

    @Test
    @DisplayName("Valid happy path transitions should be permitted")
    void testHappyPathTransitions() {
        assertDoesNotThrow(() -> OrderStateMachine.assertTransition(
                OrderStateMachine.Status.PENDING, OrderStateMachine.Status.CONFIRMED));
        assertDoesNotThrow(() -> OrderStateMachine.assertTransition(
                OrderStateMachine.Status.CONFIRMED, OrderStateMachine.Status.PAID));
        assertDoesNotThrow(() -> OrderStateMachine.assertTransition(
                OrderStateMachine.Status.PAID, OrderStateMachine.Status.PICKING));
        assertDoesNotThrow(() -> OrderStateMachine.assertTransition(
                OrderStateMachine.Status.PICKING, OrderStateMachine.Status.IN_TRANSIT));
        assertDoesNotThrow(() -> OrderStateMachine.assertTransition(
                OrderStateMachine.Status.IN_TRANSIT, OrderStateMachine.Status.DELIVERED));
        assertDoesNotThrow(() -> OrderStateMachine.assertTransition(
                OrderStateMachine.Status.DELIVERED, OrderStateMachine.Status.COMPLETED));
    }

    @Test
    @DisplayName("Illegal transition should throw BusinessRuleException")
    void testIllegalTransition() {
        assertThrows(BusinessRuleException.class, () ->
                OrderStateMachine.assertTransition(
                        OrderStateMachine.Status.PENDING, OrderStateMachine.Status.DELIVERED));
    }

    @Test
    @DisplayName("Terminal states should have no forward transitions")
    void testTerminalStates() {
        assertTrue(OrderStateMachine.allowedTransitions(OrderStateMachine.Status.CANCELLED).isEmpty());
        assertThrows(BusinessRuleException.class, () ->
                OrderStateMachine.assertTransition(
                        OrderStateMachine.Status.CANCELLED, OrderStateMachine.Status.CONFIRMED));
    }
}

