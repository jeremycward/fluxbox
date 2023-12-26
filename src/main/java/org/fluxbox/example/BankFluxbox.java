package org.fluxbox.example;

import org.fluxbox.example.model.Bank;
import org.fluxbox.example.model.BankWrapper;
import org.fluxbox.fluxbox.defaultImpl.DefaultFluxbox;

public class BankFluxbox extends DefaultFluxbox {

    public BankFluxbox(Bank b) {
        super(new BankIntray(), new BankWrapper(b));
    }

}
