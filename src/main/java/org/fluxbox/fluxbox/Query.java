package org.fluxbox.fluxbox;




import java.util.concurrent.*;

public class  Query<D,R> implements FluxboxMsg{
    private final CompletableFuture<R> futureResult;
    private final QueryFunctor<D,R> queryRunner;


    public Query(QueryFunctor queryRunner) {
        this.futureResult = new CompletableFuture<>();
        this.queryRunner = queryRunner;
    }
    public  void asyncComplete(R result){
        futureResult.complete(result);
    }

    public QueryFunctor<D, R> getQueryRunner() {
        return queryRunner;
    }

    public Future<R> result(){
        return futureResult;
    }
}
