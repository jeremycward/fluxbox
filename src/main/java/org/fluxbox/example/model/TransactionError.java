package org.fluxbox.example.model;

public class TransactionError extends RuntimeException{
    public enum ErrorTypes{
        UNKNOWN_ACCOUNT,
        INSUFFICIENT_FUNDS
    }
}
