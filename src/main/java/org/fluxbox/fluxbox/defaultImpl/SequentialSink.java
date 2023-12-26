package org.fluxbox.fluxbox.defaultImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fluxbox.fluxbox.FluxboxMsg;
import reactor.core.publisher.Flux;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

public class SequentialSink <T extends  FluxboxMsg>{
    Logger LOGGER = LogManager.getLogger(SequentialSink.class.getName());

    private final Executor putterThreadpool = Executors.newSingleThreadExecutor();

    private final Flux<T> inputFlux;

    private final BlockingQueue<FluxboxMsg> queue = new SynchronousQueue<>();

    public SequentialSink() {
        inputFlux = Flux.generate(
                () -> this.queue,
                (queue, sink) -> {
                    try {
                        T msg = (T) queue.take();
                        sink.next(msg);
                    } catch (InterruptedException e) {
                        sink.error(e);
                    }
                    return queue;
                });

    }
    public void despatch(FluxboxMsg fluxboxMsg){

        putterThreadpool.execute(()-> {
            try {
                queue.put(fluxboxMsg);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Flux<T> getInputFlux() {
        return inputFlux;
    }
}
