package org.fluxbox.fluxbox;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface QueryFunctor<T,R>  {
    R execute(T model);

}
