package io.learning.statemachine.domain;

public enum PaymentStates {
    NEW,
    PRE_AUTH,
    PRE_AUTH_ERROR,
    AUTH,
    AUTH_ERROR
}
