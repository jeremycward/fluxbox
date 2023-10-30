package cellgraph;

import graphql.util.Pair;
import org.fluxbox.spring.dto.Edge;
import org.fluxbox.spring.dto.FlatGraph;
import org.fluxbox.spring.dto.Vertex;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.graphml.GraphMLExporter;
import org.jgrapht.traverse.BreadthFirstIterator;
import reactor.core.publisher.Flux;

import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CellGraph {


    Graph<Cell, DependencyEdge> graph =
            new DefaultDirectedGraph<>(DependencyEdge.class);

    public void addCell(Cell<?> newCell, List<Cell> feeders) {
        graph.addVertex(newCell);
        feeders.stream().forEach(feeder -> graph.addEdge(feeder, newCell, new DependencyEdge(feeder.getId(), newCell.getId())));
    }

    public Pair<Cell, Cell> findLinkPoints() {
        List<Cell> inputNodes = graph.vertexSet().stream().filter(this::isBaseCell).collect(Collectors.toUnmodifiableList());
        List<Cell> outputNode = graph.vertexSet().stream().filter(this::isLeafCell).collect(Collectors.toUnmodifiableList());
        if (inputNodes.size() == 1 && outputNode.size() == 1) {
            return Pair.pair(inputNodes.get(0), outputNode.get(0));
        } else {
            throw new IllegalArgumentException(String.format("Incorrect number of input and output points : in [%s] out [%s]", inputNodes.size(), outputNode.size()));
        }

    }

    public Flux<Action> connect(Flux<Action> input) {
        Pair<Cell, Cell> linkPoints = findLinkPoints();
        Cell inputCell = linkPoints.first;
        Iterator<Cell<String>> iterator = new BreadthFirstIterator(graph, inputCell);
        CellConnector cc = new CellConnector(graph);
        Flux<Action> currentFlux = input;
        while (iterator.hasNext()) {
            currentFlux = cc.join(iterator.next(), currentFlux);
        }
        return currentFlux;
    }

    private boolean isBaseCell(Cell c) {
        return graph.incomingEdgesOf(c).size() == 0;
    }

    private boolean isLeafCell(Cell c) {
        return graph.outgoingEdgesOf(c).size() == 0;
    }

    public void exportDotty(OutputStream bos) {
        DOTExporter<Cell, DependencyEdge> exporter = new DOTExporter<>();
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(v.getId().split("__")[0]));
            return map;
        });
        exporter.exportGraph(graph, bos);
    }

    public void exportGraphMl(OutputStream bos) {
        GraphMLExporter<Cell, DependencyEdge> exporter = createExporter();
        exporter.exportGraph(graph, bos);
    }

    private static GraphMLExporter<Cell, DependencyEdge> createExporter() {
        /*
         * Create the exporter. The constructor parameter is a function which generates for each
         * vertex a unique identifier.
         */
        GraphMLExporter<Cell, DependencyEdge> exporter =
                new GraphMLExporter<>(v -> v.getId());

        exporter.setVertexAttributeProvider(v -> {
            Map<String, Attribute> m = new HashMap<>();
            m.put("name", DefaultAttribute.createAttribute("node-" + v.getId()));
            return m;
        });

        /*
         * Set the edge id provider.
         *
         * The exporter needs to generate for each edge a unique identifier.
         */
        exporter.setEdgeIdProvider(it -> it.getLabel());

        /*
         * The exporter may need to generate for each edge a set of attributes.
         */
        exporter.setEdgeAttributeProvider(e -> {
            Map<String, Attribute> m = new HashMap<>();
            m.put("name", DefaultAttribute.createAttribute(e.toString()));
            return m;
        });

        exporter.registerAttribute("name", GraphMLExporter.AttributeCategory.ALL, AttributeType.STRING);

        return exporter;
    }

    public Graph<Cell, DependencyEdge> getGraph() {
        return graph;
    }

    public FlatGraph exportFlatGraph() {
        List<Vertex> vertexList = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();


        graph.edgeSet().forEach((DependencyEdge edg) -> {
            edges.add(new Edge(edg.getID(), edg.getLabel(), edg.getLabel(), edg.getFrom(), edg.to(), List.of()));
        });

        Map<Integer, List<Cell>> generations = new HashMap<>();
        final AtomicInteger widthCounter = new AtomicInteger(0);
        BreadthFirstIterator<Cell, DependencyEdge> breadthFirstIterator = new BreadthFirstIterator<>(graph);
        while (breadthFirstIterator.hasNext()) {
            Cell thisOne = breadthFirstIterator.next();
            Integer depth = breadthFirstIterator.getDepth(thisOne);
            List<Cell> cells = generations.computeIfAbsent(depth, (Integer d) -> new ArrayList<Cell>());
            cells.add(thisOne);
            widthCounter.set(Math.max(widthCounter.get(), cells.size()));
        }
        generations.entrySet().forEach(entry -> {
            var ypos = entry.getKey();
            List<Cell> thisGeneration = entry.getValue();
            AtomicInteger startPos = new AtomicInteger( widthCounter.get() / 2 - thisGeneration.size() / 2);

            thisGeneration.stream().forEach(c -> {
                        vertexList.add(aVertex(c, startPos.getAndIncrement(), ypos));

                    }

            );

        });
        return new FlatGraph(vertexList, edges, generations.size(), widthCounter.get());


    }

    private static final Vertex aVertex(Cell c, int x, int y) {
        Vertex vtx = new Vertex(c.getId(),c.getId(),c.getCaption(),List.of(
                new org.fluxbox.spring.dto.Attribute("matrix_x_pos",Integer.toString(x),Integer.class.getSimpleName()),
                new org.fluxbox.spring.dto.Attribute("matrix_y_pos",Integer.toString(y),Integer.class.getSimpleName()),
                new org.fluxbox.spring.dto.Attribute("cellType",c.getClass().getSimpleName(),String.class.getSimpleName())
        ));
        return vtx;
    }
}
