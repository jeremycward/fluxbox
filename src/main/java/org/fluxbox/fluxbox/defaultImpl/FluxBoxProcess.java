package org.fluxbox.fluxbox.defaultImpl;

import org.fluxbox.fluxbox.FluxboxMsg;


/**
 *
 * @param <M> model type
 * @param <T> message type
 */
@FunctionalInterface
public interface  FluxBoxProcess<M,T> {
    FluxboxMsg handle( T msg, M model);

}
