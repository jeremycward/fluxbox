package org.fluxbox.spring.dto;

import java.util.List;

public class FlatGraph {
    List<Vertex> vertices;
    List<Edge> edges;

    public FlatGraph(List<Vertex> vertices, List<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}
