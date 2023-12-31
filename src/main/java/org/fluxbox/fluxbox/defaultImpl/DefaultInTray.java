package org.fluxbox.fluxbox.defaultImpl;

import org.fluxbox.example.model.ModelWrapper;
import org.fluxbox.fluxbox.FluxboxMsg;
import org.fluxbox.fluxbox.InTray;
import org.fluxbox.fluxbox.MsgTypeNotAccepted;
import reactor.core.publisher.Flux;

import java.util.List;


public class DefaultInTray implements InTray<FluxboxMsg> {

    private final String fluxboxName;

    private final SequentialSink<org.fluxbox.fluxbox.FluxboxMsg> sequentialSink = new SequentialSink<>();

    private final List<Class> supportedMsgTypes;

    public DefaultInTray(String fluxboxName, ModelWrapper<?> wrapper) {
        this.fluxboxName = fluxboxName;
        this.supportedMsgTypes = wrapper.supportedMesssageTypes();

    }

    public void despatch(FluxboxMsg fluxboxMsg)throws MsgTypeNotAccepted {
        if (supportedMsgTypes.contains(fluxboxMsg.getClass())){
            sequentialSink.despatch(fluxboxMsg);
        }else{
            throw new MsgTypeNotAccepted();
        }
    }

    public List<Class> supportedMessageTypes() {
        return List.copyOf(supportedMsgTypes);
    }


    public Flux<FluxboxMsg> getInputFlux() {
        return sequentialSink.getInputFlux();
    }
}
