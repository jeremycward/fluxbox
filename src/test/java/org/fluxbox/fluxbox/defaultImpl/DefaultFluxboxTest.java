package org.fluxbox.fluxbox.defaultImpl;

import graphql.com.google.common.collect.Streams;
import org.fluxbox.fluxbox.FluxboxMsg;
import org.jgrapht.alg.util.Pair;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;


class DefaultFluxboxTest {
    private final Boolean sync= Boolean.TRUE;
    private final int testBatchSize = 10000;

    class NamedThreadFact implements ThreadFactory{
        private final String name;

        public NamedThreadFact(String name) {
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,name);
        }
    }


    @Test
    public void testBasicPath(){
        class TestMessage implements FluxboxMsg{};
        FluxBoxProcess proc = (it)->it;
        DefaultInTray intray = new DefaultInTray("testIntrary", List.of(TestMessage.class));
        DefaultFluxbox fluxbox = new DefaultFluxbox(intray,proc);


        Runnable posting = ()->{
            fluxbox.getInTray().despatch(new TestMessage());
        };
        new Thread(posting).start();

        StepVerifier.create(fluxbox.getOutTray().getOutFlux())
                .expectNextCount(1)
                .thenCancel().verify();

    }
    private final Executor anExecutor(Integer i){
        return Executors.newSingleThreadExecutor(new NamedThreadFact(Integer.toString(i)));
    }
    @Test
    public void testAllProcessedInOrder()throws InterruptedException{





        List<Long> expectedSeqNums = new CopyOnWriteArrayList<>();


        FluxBoxProcess proc = (inputMsg)-> {
            return new TestUtils.TestOutputMessage((TestUtils.TestInputMsg) inputMsg,Thread.currentThread().getName());
        };

        DefaultInTray intray = new DefaultInTray("testIntrary", List.of(TestUtils.TestInputMsg.class));
        DefaultFluxbox fluxbox = new DefaultFluxbox(intray,proc);



        Executor exec = Executors.newFixedThreadPool(12);
        TestUtils.doManyTimes(exec,testBatchSize,(i->{
            synchronized (sync){
                expectedSeqNums.add(Long.valueOf(i));
                intray.despatch(new TestUtils.TestInputMsg(i));
            }
        }));







       List<FluxboxMsg> recording = new ArrayList<>(testBatchSize);
        StepVerifier.create(fluxbox.getOutTray().getOutFlux())
                .recordWith(()->recording)
                .expectNextCount(testBatchSize)
                .thenCancel().verify();


        assertTrue("recording should be batch size",recording.size()==testBatchSize);

        for (int i =0;i< testBatchSize; i++){
            final int expected =((TestUtils.TestOutputMessage) recording.get(i)).sourceMsg.seqNum.intValue();
            final int actual = expectedSeqNums.get(i).intValue();
            assertTrue("bad seqnum",expected==actual);
        }








    }








}