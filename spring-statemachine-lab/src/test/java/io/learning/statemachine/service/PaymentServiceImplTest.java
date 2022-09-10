package io.learning.statemachine.service;

import io.learning.statemachine.domain.Payment;
import io.learning.statemachine.domain.PaymentEvents;
import io.learning.statemachine.domain.PaymentStates;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    private PaymentService service;

    @Transactional
    @Test
    void test_Create_PreAuth_PreAuthAprv() {

        Payment payment = Payment.builder()
                            .amount(new BigDecimal("23434324234.12"))
                            .build();

        payment = service.create(payment);
        assertNotNull(payment);
        assertTrue(payment.getId() > -1);
        log.info("Create :\n{}", payment);

        StateMachine<PaymentStates, PaymentEvents> sm = service.preAuth(payment.getId());
        log.info("StateMachine after calling preAuth:\n{}", sm.getState().getId().name());
        Payment preAuthPayment = service.byId(payment.getId());
        log.info("Payment persisted (after preAuth):\n{}", preAuthPayment);

        sm = service.authorizePayment(payment.getId());
        log.info("StateMachine after calling authorizePayment:\n{}", sm.getState().getId().name());
        payment = service.byId(payment.getId());
        log.info("Payment persisted (after authorizePayment):\n{}", payment);

        sm = service.declineAuth(payment.getId());
        log.info("StateMachine after calling declineAuth:\n{}", sm.getState().getId().name());
        payment = service.byId(payment.getId());
        log.info("Payment persisted (after declineAuth):\n{}", payment);
    }
}