package org.fluxbox.spring.examples;

import ch.qos.logback.core.testUtil.RandomUtil;
import org.fluxbox.spring.dto.Attribute;
import org.fluxbox.spring.dto.Edge;
import org.fluxbox.spring.dto.FlatGraph;
import org.fluxbox.spring.dto.Vertex;

import java.util.List;
import java.util.UUID;

public class SampleGraphs {

            public static FlatGraph twoNodeGraph(){

                Vertex jeremy = new Vertex(UUID.randomUUID().toString(),"Jeremy Ward","Jezza", List.of(Attribute.aStringAttribute("SEX","M")));
                Vertex sheila = new Vertex(UUID.randomUUID().toString(),"Sheila Hanly","Shezza", List.of(Attribute.aStringAttribute("SEX","F")));
                Edge edge = Edge.anEdge()
                        .withId(UUID.randomUUID().toString())
                        .withNameAndCaption("marriage","married to")
                        .from(jeremy)
                        .to(sheila).build();

                return new FlatGraph(List.of(jeremy,sheila),List.of(edge));

            }

}
