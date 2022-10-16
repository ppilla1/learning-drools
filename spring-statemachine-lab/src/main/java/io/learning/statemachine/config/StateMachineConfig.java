package io.learning.statemachine.config;

import io.learning.statemachine.domain.PaymentEvents;
import io.learning.statemachine.domain.PaymentStates;
import io.learning.statemachine.service.PaymentServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import reactor.core.publisher.Mono;

import java.util.EnumSet;
import java.util.Random;

@Log4j2
@Configuration
@EnableStateMachineFactory
class StateMachineConfig extends StateMachineConfigurerAdapter<PaymentStates, PaymentEvents> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<PaymentStates, PaymentEvents> config) throws Exception {

        StateMachineListenerAdapter<PaymentStates, PaymentEvents> adapter = new StateMachineListenerAdapter<>(){

            @Override
            public void stateChanged(State<PaymentStates, PaymentEvents> from, State<PaymentStates, PaymentEvents> to) {
                log.info(String.format("StateChanged from: %s, to: %s", from+"", to+""));
            }

        };

        config.withConfiguration()
                .listener(adapter);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentStates, PaymentEvents> transitions) throws Exception {
        transitions
                .withExternal()
                    .source(PaymentStates.NEW).target(PaymentStates.NEW).event(PaymentEvents.PRE_AUTHORIZE).action(preAuthAction())
                .and()
                .withExternal()
                    .source(PaymentStates.NEW).target(PaymentStates.PRE_AUTH).event(PaymentEvents.PRE_AUTH_APPROVED)
                .and()
                .withExternal()
                    .source(PaymentStates.NEW).target(PaymentStates.PRE_AUTH_ERROR).event(PaymentEvents.PRE_AUTH_DECLINED)
                .and()
                .withExternal()
                    .source(PaymentStates.PRE_AUTH).target(PaymentStates.AUTH).event(PaymentEvents.AUTH_APPROVE)
                .and()
                .withExternal()
                    .source(PaymentStates.PRE_AUTH).target(PaymentStates.AUTH_ERROR).event(PaymentEvents.AUTH_DECLINED);
    }

    @Override
    public void configure(StateMachineStateConfigurer<PaymentStates, PaymentEvents> states) throws Exception {
        states.withStates()
                .initial(PaymentStates.NEW)
                .states(EnumSet.allOf(PaymentStates.class))
                .end(PaymentStates.AUTH)
                .end(PaymentStates.PRE_AUTH_ERROR)
                .end(PaymentStates.AUTH_ERROR);
    }

    public Action<PaymentStates, PaymentEvents> preAuthAction() {
        return context -> {
            Message<PaymentEvents> msg = MessageBuilder.withPayload(PaymentEvents.PRE_AUTH_DECLINED)
                                            .setHeader(PaymentServiceImpl.PAYMENT_ID, context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID))
                                            .build();

            if (new Random().nextInt(10) < 8) {
                msg = MessageBuilder.withPayload(PaymentEvents.PRE_AUTH_APPROVED)
                        .setHeader(PaymentServiceImpl.PAYMENT_ID, context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID))
                        .build();
                context.getStateMachine()
                        .sendEvent(Mono.just(msg))
                        .subscribe();
                return;
            }

            context.getStateMachine()
                    .sendEvent(Mono.just(msg))
                    .subscribe();

        };
    }
}
