package org.fluxbox.example.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fluxbox.fluxbox.FluxboxMsg;
import org.fluxbox.fluxbox.defaultImpl.FluxBoxProcess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ModelWrapper<M> {

    public static final class Builder<M, T > {
        private final Map<Class, FluxBoxProcess<M,? super FluxboxMsg>> handlerMap = new HashMap<>();
        private   M model = null;

        public Builder<M,T> withModel(M model) {
            this.model = model;
            return this;
        }

        public Builder<M,T>  withHandler(Class<T> messageClass, FluxBoxProcess<M,? super FluxboxMsg> typedHandler) {
            handlerMap.put(messageClass, typedHandler);
            return this;
        }

        public ModelWrapper<M> build() {
            return new ModelWrapper<>(handlerMap,model);
        }
    }

    public static final Builder abuilder() {
        return new Builder();

    }


    private final M model;

    private final Map<Class, FluxBoxProcess<M,? super FluxboxMsg>> handlerMap;

    public ModelWrapper(Map<Class, FluxBoxProcess<M,? super FluxboxMsg>> handlerMap, M model) {
        this.handlerMap = handlerMap;
        this.model = model;
    }

    public List<Class> supportedMesssageTypes() {
        return handlerMap.keySet().stream().toList();
    }


    public FluxboxMsg handle(FluxboxMsg msg) {
        return handlerMap.get(msg.getClass()).handle(msg, model);
    }


    static final Logger LOG = LogManager.getLogger(ModelWrapper.class.getName());


}
