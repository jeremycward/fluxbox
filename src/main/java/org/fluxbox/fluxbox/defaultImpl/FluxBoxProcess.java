package org.fluxbox.fluxbox.defaultImpl;

import org.fluxbox.fluxbox.FluxboxMsg;

@FunctionalInterface
public interface FluxBoxProcess {
    FluxboxMsg handle(FluxboxMsg input);
}
