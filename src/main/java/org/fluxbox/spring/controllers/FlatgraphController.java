package org.fluxbox.spring.controllers;

import org.fluxbox.spring.dto.FlatGraph;
import org.fluxbox.spring.examples.SampleGraphs;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;



@Controller
public class FlatgraphController {
    @QueryMapping
    public FlatGraph flatGraph(){
        return SampleGraphs.majorCcyGraph();
    }
}
