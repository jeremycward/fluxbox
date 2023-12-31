package org.fluxbox.example.msg;

import org.fluxbox.example.model.Bank;
import org.fluxbox.fluxbox.Query;
import org.fluxbox.fluxbox.QueryFunctor;

public class AccountQueryMsg extends Query<Bank,Float> {

    private static  QueryFunctor<Bank,Float> supply(String accountCode){
        return (Bank b)->b.balance(accountCode);
    }

    public AccountQueryMsg(String accountCode) {
        super(supply(accountCode));
    }
}
