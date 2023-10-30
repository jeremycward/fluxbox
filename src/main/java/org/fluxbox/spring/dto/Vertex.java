package org.fluxbox.spring.dto;


import graphql.schema.FieldCoordinates;

import java.util.List;

public class Vertex {
    private final String id;
    private final String name;


    private final String caption;



    private final List<Attribute> attributes;

    public Vertex(String id, String name, String caption, List<Attribute> attributes) {
        this.id = id;
        this.name = name;
        this.caption = caption;
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

    public List<Attribute> getAttributes() {
        return attributes;
    }
}
