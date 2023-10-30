package org.fluxbox.spring.dto;

import java.util.List;

public class FlatGraph {
    List<Vertex> vertices;
    List<Edge> edges;

    Integer matrixHeight;
    Integer matrixWidth;


    public FlatGraph(List<Vertex> vertices, List<Edge> edges, Integer matrixHeight, Integer matrixWidth) {
        this.vertices = vertices;
        this.edges =edges;
        this.matrixHeight = matrixHeight;
        this.matrixWidth = matrixWidth;

    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public Integer getMatrixHeight() {
        return matrixHeight;
    }

    public Integer getMatrixWidth() {
        return matrixWidth;
    }
}
