package org.fluxbox.example.msg;

import org.fluxbox.example.model.Transaction;
import org.fluxbox.fluxbox.Command;

import java.util.List;

public class TransactionMsg implements Command {
    private final List<Transaction> transactionList;

    public TransactionMsg(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }
}
