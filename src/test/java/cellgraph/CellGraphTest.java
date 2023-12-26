package cellgraph;

import org.fluxbox.spring.dto.FlatGraph;
import org.fluxbox.spring.dto.Vertex;
import org.fluxbox.spring.examples.SampleGraphs;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellGraphTest {

    @Test
    public void testFlatGraphMatrix(){

        FlatGraph fg = SampleGraphs.majorCcyGraph();
        assertEquals(6, fg.getMatrixHeight());
        assertEquals(18, fg.getMatrixWidth());
        assertEquals(32, fg.getVertices().size());


        Vertex first = fg.getVertices().get(0);
        assertEquals("9",first.getAttributes().stream()
                .filter(it->it.getName().equals("matrix_x_pos")).findFirst().get().getValue()
        );
        assertEquals("0",first.getAttributes().stream()
                .filter(it->it.getName().equals("matrix_y_pos")).findFirst().get().getValue()
        );

        Vertex last = fg.getVertices().get(fg.getVertices().size()-1);
        assertEquals("9",last.getAttributes().stream()
                .filter(it->it.getName().equals("matrix_x_pos")).findFirst().get().getValue()
        );
        assertEquals("5",last.getAttributes().stream()
                .filter(it->it.getName().equals("matrix_y_pos")).findFirst().get().getValue()
        );


    }

}