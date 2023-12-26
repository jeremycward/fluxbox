package org.fluxbox.fluxbox;


import java.util.concurrent.*;

public class  Query<D,R> implements FluxboxMsg{
    private final QueryFunctor queryRunner;
    private final Future<R> result=null;

    public Query(QueryFunctor queryRunner) {

        this.queryRunner = queryRunner;
    }

    public R waitForResult()throws InterruptedException, CancellationException, ExecutionException {
        return result.get();
    }


}
