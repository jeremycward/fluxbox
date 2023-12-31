package org.fluxbox.example.model;

public class TransactionError extends RuntimeException{
    public  enum ErrorTypes{
        INSUFFICIENT_FUNDS,
        BAD_ACCOUNT_NOS
    }
    private final ErrorTypes type;
    private final Transaction thrownBy;

    public TransactionError(ErrorTypes type, Transaction thrownBy) {
        this.type = type;
        this.thrownBy = thrownBy;
    }

    public ErrorTypes getType() {
        return type;
    }

    public Transaction getThrownBy() {
        return thrownBy;
    }
}
