package org.fluxbox.example.model;

import org.fluxbox.fluxbox.FluxboxMsg;
import org.fluxbox.fluxbox.Query;

public class QueryCompleteMsg implements FluxboxMsg {
    private final Query originalQuery;

    public QueryCompleteMsg(Query originalQuery) {
        this.originalQuery = originalQuery;
    }

}
