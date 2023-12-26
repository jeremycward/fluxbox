package org.fluxbox.fluxbox.defaultImpl;

import graphql.com.google.common.collect.Streams;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fluxbox.fluxbox.MsgTypeNotAccepted;
import org.jgrapht.alg.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.fluxbox.fluxbox.defaultImpl.TestUtils.takeNextExplosively;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultInTrayTest {

    static final Logger LOG = LogManager.getLogger(DefaultInTrayTest.class.getName());
    private class InvalidMdg extends TestUtils.TestInputMsg {
        public InvalidMdg(Integer seqNum) {
            super(seqNum);
        }
    };

    private DefaultInTray<TestUtils.TestInputMsg> underTest;
    private String fluxBoxName;

    private final int testBatchSize = 10000;


    @BeforeEach
    public void setup(){
        fluxBoxName = RandomStringUtils.randomAlphabetic(1);
        underTest = new DefaultInTray<>(fluxBoxName,List.of(TestUtils.TestInputMsg.class));
    }
    @Test
    public void testOneMessageGoldenPath(){
        underTest.despatch(new TestUtils.TestInputMsg(1));
        StepVerifier.create(underTest.getInputFlux())
                .expectNextCount(1)
                .thenCancel().verify(Duration.of(5000, ChronoUnit.MILLIS));

    }
    @Test
    public void testInvalidMsg(){

         assertThrows(
                MsgTypeNotAccepted.class, ()->{
                    underTest.despatch(new InvalidMdg(1));
                },"Expected Exception"
        );
    }
    public static class Recorder implements Supplier<List<TestUtils.TestInputMsg>>{
        public final List<TestUtils.TestInputMsg> recording = new CopyOnWriteArrayList<>();

        @Override
        public List<TestUtils.TestInputMsg> get() {
            return recording;
        }
    }
    @Test void testManyMsgs()throws InterruptedException{

        Recorder recorder = new Recorder();
        List<Long> expectedSeqNums = new CopyOnWriteArrayList<>();


        Executor exec = Executors.newFixedThreadPool(12);
        TestUtils.doManyTimes(exec,testBatchSize,(i->{
            synchronized (sync){
                expectedSeqNums.add(Long.valueOf(i));
                underTest.despatch(new TestUtils.TestInputMsg(i));
            }

        }));


        StepVerifier.create(underTest.getInputFlux())
                .recordWith(recorder)
                .expectNextCount(testBatchSize)
                .thenCancel()
                .verify();

        assertEquals(testBatchSize, recorder.recording.size());
        BiFunction<TestUtils.TestInputMsg,Long, Pair<TestUtils.TestInputMsg,Long>> joiner = (msg,l)->{
            return Pair.of(msg,l);
        };

        Streams.zip(recorder.recording.stream(),expectedSeqNums.stream(),joiner).forEach(
                el ->{
                    assertEquals(el.getFirst().seqNum, el.getSecond().intValue());
                    LOG.debug("msg processed: " + el.getFirst());
                }
        );

    }
    private final Boolean sync= Boolean.TRUE;

    @Test
    public void testMytheory(){
        final BlockingQueue<Long> q = new SynchronousQueue<>();
        final CopyOnWriteArrayList<Long> sentItems = new CopyOnWriteArrayList();

        Flux<Long> generator = Flux.generate(
                () -> q,
                (state, sink) -> {
                    Long nextItem =takeNextExplosively(state);
                    sink.next(nextItem);
                    return q;
                });

        Executor exec = Executors.newFixedThreadPool(12);
        TestUtils.doManyTimes(exec,testBatchSize,(i->{

            try {
                synchronized (sync){
                    sentItems.add(Long.valueOf(i));
                    q.put((Long.valueOf(i)));
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }));

        List<Long> recording = new CopyOnWriteArrayList<>();
        StepVerifier.create(generator)
                .recordWith(()->recording)
                .expectNextCount(testBatchSize )
                .thenCancel()
                .verify(Duration.ofSeconds(2));
        assertEquals(recording,sentItems);

    }
}