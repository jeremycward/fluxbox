package org.fluxbox.example;

import org.fluxbox.example.model.Bank;
import org.fluxbox.example.model.Transaction;
import org.fluxbox.example.msg.TransactionMsg;
import org.fluxbox.fluxbox.Query;
import org.fluxbox.fluxbox.QueryFunctor;
import org.fluxbox.fluxbox.defaultImpl.DefaultQueryExecutor;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

class BankFluxboxTest {
    Bank b = new Bank(50.0f);

    BankFluxbox underTest = new BankFluxbox(b);

    @Test
    public void testGoldenPath(){
        String sourceAccount = b.accounts().get(0);
        String destAccount = b.accounts().get(1);
        Runnable r = ()->{
            underTest.getInTray().despatch(new TransactionMsg(List.of(new Transaction(0, 1.0f,sourceAccount,destAccount))));
        };
        new Thread(r).start();

        StepVerifier.create(underTest.getOutTray().getOutFlux())

                .expectNextCount(1)
                .thenCancel()
                .verify(Duration.ofSeconds(1));


        QueryFunctor<Bank,Float> balanceEnquiry= (b)->b.balance(sourceAccount);









    }

}



