package io.learning.statemachine.domain;

public enum PaymentEvents {
    PRE_AUTHORIZE,
    PRE_AUTH_APPROVED,
    PRE_AUTH_DECLINED,
    AUTHORIZE,
    AUTH_APPROVE,
    AUTH_DECLINED
}
