package org.fluxbox.example;

import org.fluxbox.example.msg.TransactionMsg;
import org.fluxbox.fluxbox.defaultImpl.DefaultInTray;

import java.util.List;

public class BankIntray extends DefaultInTray<TransactionMsg> {
    public BankIntray() {
        super("BankIntray", List.of(TransactionMsg.class));
    }
}
