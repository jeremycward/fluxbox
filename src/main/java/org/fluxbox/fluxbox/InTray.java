package org.fluxbox.fluxbox;

import reactor.core.publisher.Flux;

import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;

public   interface InTray<T extends FluxboxMsg> {

    public void despatch(T msg);

    public Flux<T> getInputFlux();


}
