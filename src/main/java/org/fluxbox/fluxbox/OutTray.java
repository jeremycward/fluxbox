package org.fluxbox.fluxbox;

import reactor.core.publisher.Flux;

public interface OutTray {
    public Flux<FluxboxMsg> getOutFlux();

}
