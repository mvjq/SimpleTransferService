package com.example.simpletransferservice.domain.enums;

import com.example.simpletransferservice.domain.model.Transaction;

import javax.security.auth.login.FailedLoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum TransactionStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    AUTH_DENIED,
    FAILED;

    private static final Map<TransactionStatus, Set<TransactionStatus>> TRANSITIONS = new HashMap<>();

    static {
        TRANSITIONS.put(PENDING, Set.of(PROCESSING, AUTH_DENIED));
        TRANSITIONS.put(PROCESSING, Set.of(COMPLETED, FAILED));
        TRANSITIONS.put(COMPLETED, Set.of());
        TRANSITIONS.put(AUTH_DENIED, Set.of());
        TRANSITIONS.put(FAILED, Set.of());
    }

    public Set<TransactionStatus> getPossibleTransitions() {
        return TRANSITIONS.getOrDefault(this, Set.of());
    }

    public boolean canTransitionTo(TransactionStatus newStatus) {
        return getPossibleTransitions().contains(newStatus);
    }

    public boolean isFinal() {
        return getPossibleTransitions().isEmpty();
    }
}
