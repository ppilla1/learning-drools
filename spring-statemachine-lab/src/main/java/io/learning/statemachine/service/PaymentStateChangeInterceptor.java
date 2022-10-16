package io.learning.statemachine.service;

import io.learning.statemachine.domain.Payment;
import io.learning.statemachine.domain.PaymentEvents;
import io.learning.statemachine.domain.PaymentStates;
import io.learning.statemachine.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Component
class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentStates, PaymentEvents> {

    private final PaymentRepository repository;

    @Override
    public void preStateChange(State<PaymentStates, PaymentEvents> state, Message<PaymentEvents> message, Transition<PaymentStates, PaymentEvents> transition, StateMachine<PaymentStates, PaymentEvents> stateMachine, StateMachine<PaymentStates, PaymentEvents> rootStateMachine) {
        Optional.ofNullable(message)
                .ifPresent(msg -> {

                    Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(PaymentServiceImpl.PAYMENT_ID, -1L)))
                            .ifPresent(id -> {

                                Payment pay = repository.getReferenceById(id);
                                pay.setState(state.getId());
                                repository.save(pay);

                            });

                });
    }
}
