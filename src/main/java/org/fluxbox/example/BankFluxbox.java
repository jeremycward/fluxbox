package org.fluxbox.example;

import org.fluxbox.example.model.*;
import org.fluxbox.example.msg.AccountQueryMsg;
import org.fluxbox.example.msg.TransactionMsg;
import org.fluxbox.fluxbox.defaultImpl.DefaultFluxbox;
import org.fluxbox.fluxbox.defaultImpl.FluxBoxProcess;

import java.util.List;
import java.util.Optional;

public class BankFluxbox extends DefaultFluxbox {

    private static final FluxBoxProcess<Bank,TransactionMsg> transactionHandler = (msg, model) -> {
        List<Notification> notificationList = null;
        try {
            notificationList = model.process(msg.getTransaction());
            return new TransactionResultMsg(msg.getTransaction(),notificationList, Optional.empty());
        } catch (TransactionError e) {
            return new TransactionResultMsg(msg.getTransaction(), List.of(), Optional.of(e));
        }

    };
    private static final FluxBoxProcess<Bank,AccountQueryMsg> balanceQueryHandler = (msg, model) -> {
        msg.asyncComplete(msg.getQueryRunner().execute(model));
        return new QueryCompleteMsg(msg);
    };





    public BankFluxbox(Bank b) {
        super(new ModelWrapper.Builder()
                .withModel(b)
                .withHandler(TransactionMsg.class,transactionHandler)
                .withHandler(AccountQueryMsg.class, balanceQueryHandler)
                .build());
    }

}
