package org.fluxbox.spring.dto;


import java.util.ArrayList;
import java.util.List;

public class Edge {
    private final String id;
    private final String name;
    private final String caption;

    private final String from;
    private final String to;

    private final List<Attribute> attributes;

    public Edge(String id, String name, String caption, String from, String to, List<Attribute> attributes) {
        this.id = id;
        this.name = name;
        this.caption = caption;
        this.from = from;
        this.to = to;
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCaption() {
        return caption;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
    public static EdgeBuilder anEdge(){
        return new EdgeBuilder();
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }
    public static final class EdgeBuilder {
        private String from;
        private String to;
        private String name;
        private String caption;
        private String id;
        private List<Attribute> attributes= new ArrayList<>();

        public EdgeBuilder from(Vertex from){
            this.from = from.getId();
            return this;
        }
        public EdgeBuilder to(Vertex to){
            this.to = to.getId();
            return this;
        }
        public EdgeBuilder withNameAndCaption(String name,String caption){
            this.name = name;
            this.caption = caption;
            return this;
        }
        public EdgeBuilder withId(String id){
            this.id = id;
            return this;
        }
        public Edge build(){
            return new Edge(id,name,caption,from,to,attributes);
        }

    }
}
