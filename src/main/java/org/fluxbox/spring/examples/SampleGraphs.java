package org.fluxbox.spring.examples;

import ch.qos.logback.core.testUtil.RandomUtil;
import org.fluxbox.spring.dto.Attribute;
import org.fluxbox.spring.dto.Edge;
import org.fluxbox.spring.dto.FlatGraph;
import org.fluxbox.spring.dto.Vertex;

import java.util.Currency;
import java.util.List;
import java.util.UUID;

public class SampleGraphs {


   public static FlatGraph majorCcyGraph(){
        String [] longTenors = {"1Y","2Y","3Y"};
        String [] shortTenors = {"1M","2M","3M"};
        Currency [] ccys = {
                Currency.getInstance("EUR"),
                Currency.getInstance("USD"),
                Currency.getInstance("JPY")
        };
        MajorCcyGraph gr = MajorCcyGraph.aCcyGraph()
                .withShortTenors(shortTenors)
                .withLongTenors(longTenors)
                .withCcys("EUR","USD","JPY")
                .build();

        return gr.getCellGraph().exportFlatGraph();
   }

}
