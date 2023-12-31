package org.fluxbox.example;

import org.fluxbox.example.model.Bank;
import org.fluxbox.example.model.Notification;
import org.fluxbox.example.model.Transaction;
import org.fluxbox.example.model.TransactionError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


public class NakedBankTest {
    Bank underTest;
    long transactionId = 0;



    @BeforeEach
    public void init(){
        transactionId = 1;
        underTest = new Bank(100.0f);
    }
    @Test
    public void testSimpleTransfer(){
        String sourceAcc =  underTest.accounts().get(0);
        String targetAcc =  underTest.accounts().get(1);
        assertThat(underTest.balance(sourceAcc).intValue(),is(100));
        assertThat(underTest.balance(targetAcc).intValue(),is(100));

        Transaction tr = new Transaction(transactionId++,100,sourceAcc,targetAcc);
        List<Notification> notificationList =underTest.process(tr);
        assertThat(underTest.balance(sourceAcc).intValue(),is(0));
        assertThat(underTest.balance(targetAcc).intValue(),is(200));
        assertThat(notificationList.size(),is(1));
        assertThat(notificationList.get(0).getType(),is(Notification.NotificationTypes.TRANSACTION_COMPLETED));
        assertThat(notificationList.get(0).getTransactionId(),is(tr.transactionId()));

    }
    @Test
    public void testAttemptTotransferBetweenSameAccounts(){
        Transaction tr = new Transaction(transactionId++,100,underTest.accounts().get(0),underTest.accounts().get(0));
        Optional<TransactionError> te = Optional.empty();
        try {
            underTest.process(tr);
            fail("Exception should have been thown");
        } catch (TransactionError e) {
            te = Optional.of(e);
        }
        assertThat(te.get().getType(),is(TransactionError.ErrorTypes.BAD_ACCOUNT_NOS));
        assertThat(te.get().getThrownBy().transactionId(),is(tr.transactionId()));
    }

    @Test
    public void testAttemptTotransferFromBadAccount(){
        Transaction tr = new Transaction(transactionId++,100,"badAcNumber",underTest.accounts().get(1));
        Optional<TransactionError> te = Optional.empty();
        try {
            underTest.process(tr);
            fail("Exception should have been thown");
        } catch (TransactionError e) {
            te = Optional.of(e);
        }
        assertThat(te.get().getType(),is(TransactionError.ErrorTypes.BAD_ACCOUNT_NOS));
        assertThat(te.get().getThrownBy().transactionId(),is(tr.transactionId()));
    }
    @Test
    public void testAttemptToTransferWithInsufficentFunds(){
        Transaction tr = new Transaction(transactionId++,500,underTest.accounts().get(0),underTest.accounts().get(1));
        Optional<TransactionError> te = Optional.empty();
        try {
            underTest.process(tr);
            fail("Exception should have been thown");
        } catch (TransactionError e) {
            te = Optional.of(e);
        }
        assertThat(te.get().getType(),is(TransactionError.ErrorTypes.INSUFFICIENT_FUNDS));
        assertThat(te.get().getThrownBy().transactionId(),is(tr.transactionId()));

    }
    @Test
    public void testTransferResultinginPermittedOverdraft(){
        Transaction tr = new Transaction(transactionId++,150,underTest.accounts().get(0),underTest.accounts().get(1));
        List<Notification> notificationList = underTest.process(tr);
        assertThat(notificationList.size(),is(2));
        assertThat(notificationList.get(0).getType(),is(Notification.NotificationTypes.TRANSACTION_COMPLETED));
        assertThat(notificationList.get(1).getType(),is(Notification.NotificationTypes.OVERDRAFT_WARNING));

    }
    @Test
    public void testTransactionAttemptWhenBankClosed(){
        Bank.closeBank();
        Transaction tr = new Transaction(transactionId++,1,underTest.accounts().get(0),underTest.accounts().get(1));
        try {
            underTest.process(tr);
            fail("exception should have been thrown");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(),equalTo("bank is closed"));
        }


    }



}
