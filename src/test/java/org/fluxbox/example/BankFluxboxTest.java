package org.fluxbox.example;

import graphql.Assert;
import org.fluxbox.example.model.*;
import org.fluxbox.example.msg.AccountQueryMsg;
import org.fluxbox.example.msg.TransactionMsg;
import org.fluxbox.fluxbox.FluxboxMsg;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


class BankFluxboxTest {
    Bank b = new Bank(100.0f);

    BankFluxbox underTest = new BankFluxbox(b);

    @Test
    public void testGoldenPath() throws Exception {
        String sourceAccount = b.accounts().get(0);
        String destAccount = b.accounts().get(1);

        AccountQueryMsg queryMsgAfter = new AccountQueryMsg(sourceAccount);
        AccountQueryMsg queryMsgBefore = new AccountQueryMsg(sourceAccount);

        underTest.getInTray().despatch(queryMsgBefore);
        underTest.getInTray().despatch(new TransactionMsg(new Transaction(0, 1.0f, sourceAccount, destAccount)));
        underTest.getInTray().despatch(queryMsgAfter);

        List<FluxboxMsg> recording = new CopyOnWriteArrayList<>();
        StepVerifier.create(underTest.getOutTray().getOutFlux())
                .recordWith(() -> recording)
                .consumeNextWith(msg -> {
                    assertThat(msg.getClass(), equalTo(QueryCompleteMsg.class));
                })
                .consumeNextWith(msg -> {
                    assertThat(msg.getClass(), equalTo(TransactionResultMsg.class));
                })
                .consumeNextWith(msg -> {
                    assertThat(msg.getClass(), equalTo(QueryCompleteMsg.class));
                })
                .thenCancel()
                .verify(Duration.ofSeconds(1));

        Float balanceBefore = queryMsgBefore.result().get();
        Float balanceAfter = queryMsgAfter.result().get();
        Assert.assertTrue(((int) (balanceBefore - balanceAfter)) == 1);

    }

    @Test
    public void testTransferAttemptWithIllegalAccountCode() {
        Transaction illegalTransaction =
                new Transaction(0, 2.0f, "", "");
        TransactionMsg msg = new TransactionMsg(illegalTransaction);

        underTest.getInTray().despatch(msg);

        StepVerifier.create(underTest.getOutTray().getOutFlux())
                .consumeNextWith(outMsg -> {
                    TransactionResultMsg resultMsg = (TransactionResultMsg) outMsg;
                    assertTrue(resultMsg.getError().isPresent());
                    assertTrue(resultMsg.getNotificationList().isEmpty());
                    assertThat(resultMsg.getTransaction().transactionId(), equalTo(msg.getTransaction().transactionId()));
                })
                .thenCancel().verify();
    }

    @Test
    public void testTransferAttemptResultingInOverdraft()throws ExecutionException, InterruptedException {
        String sourceAccount = b.accounts().get(0);
        String destAccount = b.accounts().get(1);
        AccountQueryMsg queryMsgAfter = new AccountQueryMsg(sourceAccount);

        Transaction trans = new Transaction(0, 150.0f, sourceAccount, destAccount);
        TransactionMsg msg = new TransactionMsg(trans);

        underTest.getInTray().despatch(msg);
        underTest.getInTray().despatch(queryMsgAfter);

        StepVerifier.create(underTest.getOutTray().getOutFlux())
                .consumeNextWith(outMsg -> {
                    TransactionResultMsg resultMsg = (TransactionResultMsg) outMsg;
                    assertTrue(resultMsg.getError().isEmpty());
                    assertThat(resultMsg.getNotificationList().size(), equalTo(2));
                    assertThat(resultMsg.getNotificationList().get(0).getType(), is(Notification.NotificationTypes.TRANSACTION_COMPLETED));
                    assertThat(resultMsg.getNotificationList().get(1).getType(), is(Notification.NotificationTypes.OVERDRAFT_WARNING));
                    assertThat(resultMsg.getTransaction().transactionId(), equalTo(msg.getTransaction().transactionId()));

                })
                .consumeNextWith(qMsg -> {
                            assertThat(qMsg.getClass(), equalTo(QueryCompleteMsg.class));
                        }

                )

                .thenCancel().verify();
        Float afterTransfer = queryMsgAfter.result().get();
        assertThat(afterTransfer.intValue(),equalTo(-50));
    }
    @Test
    public void testTransferAttemptWhenBankClosed()throws ExecutionException, InterruptedException {

        String sourceAccount = b.accounts().get(0);
        String destAccount = b.accounts().get(1);
        AccountQueryMsg queryMsgAfter = new AccountQueryMsg(sourceAccount);

        Transaction trans = new Transaction(0, 1.0f, sourceAccount, destAccount);
        TransactionMsg msg = new TransactionMsg(trans);
        Bank.closeBank();
        underTest.getInTray().despatch(msg);
        StepVerifier.create(underTest.getOutTray().getOutFlux()).expectError().verify();
    }

}



