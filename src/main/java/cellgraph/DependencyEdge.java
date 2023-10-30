package cellgraph;

import org.jgrapht.graph.DefaultEdge;

import java.util.UUID;

public class DependencyEdge extends DefaultEdge {
    private final   String label="Depends On";
    private final String from;
    private final String to;

    private final String ID = UUID.randomUUID().toString();

    /**
     * Constructs a relationship edge
     *
     * @param label the label of the new edge.
     */
    public DependencyEdge(String from, String to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Gets the label associated with this edge.
     *
     * @return edge label
     */
    public String getLabel() {
        return label;
    }
    public String from(){
        return from;

    }
    public String to(){
        return to;

    }

    @Override
    public String toString() {
        return "(" + getSource() + " : " + getTarget() + " : " + label + ")";
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getID() {
        return ID;
    }
}

