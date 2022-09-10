package io.learning.statemachine.service;

import io.learning.statemachine.domain.Payment;
import io.learning.statemachine.domain.PaymentEvents;
import io.learning.statemachine.domain.PaymentStates;
import io.learning.statemachine.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    public static final String PAYMENT_ID = "paymentId";
    private final StateMachineFactory<PaymentStates, PaymentEvents> factory;
    private final PaymentRepository repository;
    private final PaymentStateChangeInterceptor paymentStateChangeInterceptor;

    @Override
    public Payment byId(Long paymentId) {
        return repository.getReferenceById(paymentId);
    }

    @Override
    public Payment create(Payment payment) {
        payment.setState(PaymentStates.NEW);
        return repository.save(payment);
    }

    @Override
    public StateMachine<PaymentStates, PaymentEvents> preAuth(Long paymentId) {
        return sendEvent(paymentId, PaymentEvents.PRE_AUTHORIZE);
    }

    @Override
    public StateMachine<PaymentStates, PaymentEvents> authorizePayment(Long paymentId) {
        return sendEvent(paymentId, PaymentEvents.AUTH_APPROVE);
    }

    @Override
    public StateMachine<PaymentStates, PaymentEvents> declineAuth(Long paymentId) {
        return sendEvent(paymentId, PaymentEvents.AUTH_DECLINED);
    }

    private StateMachine<PaymentStates, PaymentEvents> sendEvent(Long paymentId, PaymentEvents event) {
        StateMachine<PaymentStates, PaymentEvents> sm = rehydrateStateMachine(paymentId);

        Message<PaymentEvents>  eventMsg = MessageBuilder.withPayload(event)
                                            .setHeader(PAYMENT_ID, paymentId)
                                            .build();

        sm.sendEvent(Mono.just(eventMsg)).subscribe();

        return sm;
    }
    private StateMachine<PaymentStates, PaymentEvents> rehydrateStateMachine(Long paymentId) {
        Payment payment = byId(paymentId);

        StateMachine<PaymentStates, PaymentEvents> sm = factory.getStateMachine(Long.toString(payment.getId()));
        sm.stopReactively().subscribe();

        sm.getStateMachineAccessor()
                .doWithAllRegions( sma -> {
                    sma.addStateMachineInterceptor(paymentStateChangeInterceptor);
                    sma.resetStateMachineReactively(new DefaultStateMachineContext<>(payment.getState(), null, null, null)).subscribe();
                });

        sm.startReactively().subscribe();

        return sm;
    }
}
