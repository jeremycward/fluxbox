package org.fluxbox.example.msg;

import org.fluxbox.example.model.Transaction;
import org.fluxbox.fluxbox.Command;

public class TransactionMsg implements Command {
    private final Transaction transaction;


    public TransactionMsg(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
