package org.fluxbox.fluxbox.defaultImpl;

import org.fluxbox.fluxbox.QueryFunctor;

public  class DefaultQueryExecutor<D,R> implements QueryFunctor<D,R> {
    @Override
    public R execute(D model) {
        return null;
    }
}
