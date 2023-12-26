package org.fluxbox.example.model;

import org.fluxbox.example.msg.TransactionMsg;
import org.fluxbox.fluxbox.FluxboxMsg;
import org.fluxbox.fluxbox.defaultImpl.FluxBoxProcess;

public class BankWrapper implements FluxBoxProcess {
    private final Bank bank;

    public BankWrapper(Bank bank) {
        this.bank = bank;
    }

    @Override
    public FluxboxMsg handle(FluxboxMsg input) {
        TransactionMsg transactionMsg = (TransactionMsg) input;
        transactionMsg.getTransactionList().forEach(it-> bank.process(it));
        return new FluxboxMsg() {};
    }
}
