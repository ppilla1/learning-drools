package io.learning.statemachine.service;

import io.learning.statemachine.domain.Payment;
import io.learning.statemachine.domain.PaymentEvents;
import io.learning.statemachine.domain.PaymentStates;
import org.springframework.statemachine.StateMachine;

public interface PaymentService {
    Payment byId(Long paymentId);
    Payment create(Payment payment);
    StateMachine<PaymentStates, PaymentEvents> preAuth(Long paymentId);
    StateMachine<PaymentStates, PaymentEvents> authorizePayment(Long paymentId);
    StateMachine<PaymentStates, PaymentEvents> declineAuth(Long paymentId);
}
