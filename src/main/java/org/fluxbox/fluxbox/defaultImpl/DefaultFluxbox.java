package org.fluxbox.fluxbox.defaultImpl;

import org.fluxbox.fluxbox.Fluxbox;
import org.fluxbox.fluxbox.FluxboxMsg;
import org.fluxbox.fluxbox.InTray;
import org.fluxbox.fluxbox.OutTray;

public class DefaultFluxbox implements Fluxbox {
    private final DefaultInTray inTray;
      private  DefaultOutTray outTray;

    public DefaultFluxbox(DefaultInTray inTray,  FluxBoxProcess processor) {

        this.inTray = inTray;
        this.outTray =  new DefaultOutTray(inTray.getInputFlux()
                .map(in->{
                    return processor.handle((FluxboxMsg) in);
                }));
    }


    @Override
    public InTray getInTray() {
        return inTray;
    }

    @Override
    public OutTray getOutTray() {
        return outTray;
    }
}
