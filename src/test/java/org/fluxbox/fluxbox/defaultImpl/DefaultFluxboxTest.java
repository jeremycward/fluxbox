package org.fluxbox.fluxbox.defaultImpl;

import org.fluxbox.example.model.ModelWrapper;
import org.fluxbox.fluxbox.FluxboxMsg;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.springframework.test.util.AssertionErrors.assertTrue;


class DefaultFluxboxTest {
    private final Boolean sync= Boolean.TRUE;


    @Test
    public void testBasicPath(){

        class TestMessage implements FluxboxMsg{}
        ModelWrapper.Builder builder = new ModelWrapper.Builder<Boolean,TestMessage>()
                .withModel(Boolean.TRUE)
                .withHandler(TestMessage.class, (msg, model) -> new FluxboxMsg(){});

        DefaultFluxbox fluxbox = new DefaultFluxbox(builder.build());


        Runnable posting = ()-> fluxbox.getInTray().despatch(new TestMessage());
        new Thread(posting).start();

        StepVerifier.create(fluxbox.getOutTray().getOutFlux())
                .expectNextCount(1)
                .thenCancel().verify();

    }

    @Test
    public void testAllProcessedInOrder(){

        List<Long> expectedSeqNums = new CopyOnWriteArrayList<>();
        ModelWrapper<Object> wrapper = ModelWrapper.abuilder()
                .withHandler(TestUtils.TestInputMsg.class,(inputMsg, model)-> new TestUtils.TestOutputMessage((TestUtils.TestInputMsg) inputMsg,Thread.currentThread().getName()))
                .build();

        DefaultFluxbox fluxbox = new DefaultFluxbox(wrapper);



        Executor exec = Executors.newFixedThreadPool(12);
        int testBatchSize = 10000;
        TestUtils.doManyTimes(exec, testBatchSize,(i->{
            synchronized (sync){
                expectedSeqNums.add(Long.valueOf(i));
                fluxbox.getInTray().despatch(new TestUtils.TestInputMsg(i));

            }
        }));


       List<FluxboxMsg> recording = new ArrayList<>(testBatchSize);
        StepVerifier.create(fluxbox.getOutTray().getOutFlux())
                .recordWith(()->recording)
                .expectNextCount(testBatchSize)
                .thenCancel().verify();


        assertTrue("recording should be batch size",recording.size()== testBatchSize);

        for (int i = 0; i< testBatchSize; i++){
            final int expected =((TestUtils.TestOutputMessage) recording.get(i)).sourceMsg.seqNum.intValue();
            final int actual = expectedSeqNums.get(i).intValue();
            assertTrue("bad seqnum",expected==actual);
        }

    }








}