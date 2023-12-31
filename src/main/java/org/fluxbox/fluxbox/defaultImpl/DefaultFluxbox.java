package org.fluxbox.fluxbox.defaultImpl;

import org.fluxbox.example.model.ModelWrapper;
import org.fluxbox.fluxbox.Fluxbox;
import org.fluxbox.fluxbox.FluxboxMsg;
import org.fluxbox.fluxbox.InTray;
import org.fluxbox.fluxbox.OutTray;

import java.util.UUID;

public class DefaultFluxbox<T> implements Fluxbox {


    private final DefaultInTray inTray;
    private final  DefaultOutTray outTray;


    public DefaultFluxbox(String name, ModelWrapper<T> wrapper) {
        this.inTray = new DefaultInTray( name,wrapper );
        this.outTray = new DefaultOutTray(inTray.getInputFlux()
                .map((in) -> wrapper.handle((FluxboxMsg) in)));
    }

    public DefaultFluxbox(ModelWrapper<T> wrapper) {
        this (UUID.randomUUID().toString(),wrapper);
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
