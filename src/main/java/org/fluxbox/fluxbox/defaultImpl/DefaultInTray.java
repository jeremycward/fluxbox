package org.fluxbox.fluxbox.defaultImpl;

import org.fluxbox.fluxbox.FluxboxMsg;
import org.fluxbox.fluxbox.InTray;
import org.fluxbox.fluxbox.MsgTypeNotAccepted;
import reactor.core.publisher.Flux;

import java.util.List;


public class DefaultInTray<T extends FluxboxMsg> implements InTray<T> {

    private final String fluxboxName;

    private final SequentialSink<T> sequentialSink = new SequentialSink<>();

    private final List<Class<T>> supportedMsgTypes;

    public DefaultInTray(String fluxboxName, List<Class<T>> supportedMsgTypes) {
        this.fluxboxName = fluxboxName;
        this.supportedMsgTypes = supportedMsgTypes;

    }

    public void despatch(FluxboxMsg fluxboxMsg)throws MsgTypeNotAccepted {
        if (supportedMsgTypes.contains(fluxboxMsg.getClass())){
            sequentialSink.despatch(fluxboxMsg);
        }else{
            throw new MsgTypeNotAccepted();
        }


    }

    public List<Class<T>> supportedMessageTypes() {
        return List.copyOf(supportedMsgTypes);
    }

    public String getFluxboxName() {
        return fluxboxName;
    }

    public Flux<T> getInputFlux() {
        return sequentialSink.getInputFlux();
    }
}
