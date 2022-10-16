package io.learning.statemachine.config;

import io.learning.statemachine.domain.PaymentEvents;
import io.learning.statemachine.domain.PaymentStates;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class StateMachineConfigTest {

    @Autowired
    StateMachineFactory<PaymentStates, PaymentEvents> factory;

    @Test
    void testNewStateMachine() {
        UUID cardId = UUID.randomUUID();
        StateMachine<PaymentStates, PaymentEvents> sm = factory.getStateMachine(cardId);

        sm.startReactively().subscribe();

        log.info("{}", sm.getState());
        assertTrue(sm.getState().getId() == PaymentStates.NEW);

        Message<PaymentEvents> preAuthEvent = MessageBuilder.withPayload(PaymentEvents.PRE_AUTHORIZE)
                                                .setHeader("cardId", cardId)
                                                .build();

        sm.sendEvent(Mono.just(preAuthEvent)).subscribe();
      log.info("{}", sm.getState());


        Message<PaymentEvents> authApprove = MessageBuilder.withPayload(PaymentEvents.AUTH_APPROVE)
                .setHeader("cardId", cardId)
                .build();

        sm.sendEvent(Mono.just(authApprove)).subscribe();

        log.info("{}", sm.getState());

        Message<PaymentEvents> authDecline = MessageBuilder.withPayload(PaymentEvents.AUTH_DECLINED)
                .setHeader("cardId", cardId)
                .build();

        sm.sendEvent(Mono.just(authDecline)).subscribe();

        log.info("{}", sm.getState());

    }
}