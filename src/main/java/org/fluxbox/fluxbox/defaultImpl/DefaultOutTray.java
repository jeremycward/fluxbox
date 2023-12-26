package org.fluxbox.fluxbox.defaultImpl;

import org.fluxbox.fluxbox.FluxboxMsg;
import org.fluxbox.fluxbox.OutTray;
import reactor.core.publisher.Flux;

import java.util.concurrent.ArrayBlockingQueue;

public class DefaultOutTray implements OutTray {
    private final Flux<FluxboxMsg> outputFlux;

    public DefaultOutTray(Flux<FluxboxMsg> outputFlux) {
        this.outputFlux = outputFlux;
    }

    @Override
    public Flux<FluxboxMsg> getOutFlux() {
        return outputFlux;
    }
}
