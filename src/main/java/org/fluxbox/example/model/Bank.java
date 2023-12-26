package org.fluxbox.example.model;

import graphql.com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Bank {

    private Map<String,Float> accounts = Maps.newHashMap();

    public Bank(Float openingBalance ) {
        generateAccountNos(2).forEach(it->accounts.put(it,openingBalance));
    }

    public List<Notification> process(Transaction transaction)throws TransactionError{
        float sourceAc = accounts.get(transaction.sourceAc());
        float targetAc = accounts.get(transaction.targetAc());
        sourceAc -= transaction.amount();
        targetAc +=  transaction.amount();
        accounts.put(transaction.sourceAc(),sourceAc);
        accounts.put(transaction.targetAc(), targetAc);

        return List.of(new Notification());
    }
    public Map<String,Float> getStatement(){
        return Map.copyOf(accounts);
    }

    private static final List<String> generateAccountNos(int numAccounts){
        return IntStream.range(0,numAccounts)
                .boxed()
                .map(numb->numb.toString())
                .collect(Collectors.toUnmodifiableList());

    }
    public final List<String> accounts(){
        return accounts.keySet().stream().collect(Collectors.toUnmodifiableList());

    }
    public final Float balance(String account){
        return accounts.get(account);
    }

}
