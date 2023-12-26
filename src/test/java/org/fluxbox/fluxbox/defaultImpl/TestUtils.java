package org.fluxbox.fluxbox.defaultImpl;

import org.fluxbox.fluxbox.FluxboxMsg;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class TestUtils {

    public static class TestInputMsg implements FluxboxMsg {
        public final Integer seqNum;
        public final String createdOnThread;


        public TestInputMsg(Integer seqNum) {

            this.seqNum = seqNum;
            this.createdOnThread = Thread.currentThread().getName();
        }






        @Override
        public String toString() {
            return "TestInputMsg{" +
                    "seqNum=" + seqNum +
                    ", createdOnThread='" + createdOnThread + '\'' +
                    '}';
        }
    }
    public static class TestOutputMessage implements FluxboxMsg{
        public final TestInputMsg sourceMsg;
        private final String threadId;

        public TestOutputMessage(TestInputMsg sourceMsg, String threadId) {
            this.sourceMsg = sourceMsg;
            this.threadId = threadId;

        }
    }

    public static final  Long takeNextExplosively(BlockingQueue<Long> q){
        try {
            return q.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public static  void doManyTimes(Executor executor, int times, Consumer<Integer> doOnce){
        IntStream.range(1,times+1).boxed()
                .forEach(
                i -> executor.execute(()->{doOnce.accept(i);})
        );


    }

}
