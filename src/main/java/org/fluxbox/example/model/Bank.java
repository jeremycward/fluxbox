package org.fluxbox.example.model;

import graphql.com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Bank {

    private static boolean bankOpen = true;

    private static  void validate(Transaction transaction, Bank b) throws TransactionError{
        if(transaction.sourceAc().equals(transaction.targetAc())){
            throw new TransactionError(TransactionError.ErrorTypes.BAD_ACCOUNT_NOS,transaction);
        }
        boolean accountsOk =b.accounts.containsKey(transaction.targetAc()) && b.accounts.containsKey(transaction.sourceAc());
        if (!accountsOk){
            throw new TransactionError(TransactionError.ErrorTypes.BAD_ACCOUNT_NOS,transaction);
        }
        Float availableFunds  =  b.accounts.get(transaction.sourceAc());
        if ( availableFunds.floatValue() - transaction.amount() < -100){
            throw new TransactionError(TransactionError.ErrorTypes.INSUFFICIENT_FUNDS,transaction);
        }
    }

    private Map<String,Float> accounts = Maps.newHashMap();

    public Bank(Float openingBalance, int numbAccounts ) {
        generateAccountNos(numbAccounts).forEach(it->accounts.put(it,openingBalance));
    }
    public Bank(Float openingBalance) {
        this(openingBalance,2);
    }

    public List<Notification> process(Transaction transaction)throws TransactionError{
        if (!bankOpen) throw new RuntimeException("bank is closed");
        validate(transaction,this);
        float sourceAc = accounts.get(transaction.sourceAc());
        float targetAc = accounts.get(transaction.targetAc());
        sourceAc -= transaction.amount();
        targetAc +=  transaction.amount();
        accounts.put(transaction.sourceAc(),sourceAc);
        accounts.put(transaction.targetAc(), targetAc);
        List<Notification> transactionList = new ArrayList<>(
                List.of(new Notification(transaction.transactionId(), Notification.NotificationTypes.TRANSACTION_COMPLETED)
        ));
        if (sourceAc < 0){
            transactionList.add(new Notification(transaction.transactionId(), Notification.NotificationTypes.OVERDRAFT_WARNING));
        }
        return transactionList;
    }

    private static final List<String> generateAccountNos(int numAccounts){
        return IntStream.range(0,numAccounts)
                .boxed()
                .map(Object::toString)
                .toList();

    }
    public final List<String> accounts(){
        return accounts.keySet().stream().collect(Collectors.toUnmodifiableList());

    }
    public final Float balance(String account){
        return accounts.get(account);
    }

    public static  void closeBank(){
        bankOpen = false;
    }

}
