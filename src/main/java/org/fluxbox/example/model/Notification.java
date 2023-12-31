package org.fluxbox.example.model;

public class Notification {

    private final Long transactionId;
    public  enum NotificationTypes{
        TRANSACTION_COMPLETED,
        OVERDRAFT_WARNING,

    }
    private final NotificationTypes type;

    public Notification(Long transactionId, NotificationTypes type) {
        this.transactionId = transactionId;
        this.type = type;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public NotificationTypes getType() {
        return type;
    }
}
