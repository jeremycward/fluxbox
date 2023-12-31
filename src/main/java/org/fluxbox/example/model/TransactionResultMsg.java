package org.fluxbox.example.model;

import org.fluxbox.fluxbox.FluxboxMsg;

import java.util.List;
import java.util.Optional;

public class TransactionResultMsg implements FluxboxMsg {

    private final Transaction transaction;
    private final List<Notification> notificationList;

    private final Optional<TransactionError> error;

    public TransactionResultMsg(Transaction transaction, List<Notification> notificationList, Optional<TransactionError> error) {
        this.transaction = transaction;
        this.notificationList = notificationList;
        this.error = error;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public Optional<TransactionError> getError() {
        return error;
    }
}
