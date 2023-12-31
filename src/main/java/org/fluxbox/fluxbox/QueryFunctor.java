package org.fluxbox.fluxbox;

@FunctionalInterface
public interface QueryFunctor<T,R>  {
    R execute(T model);

}
