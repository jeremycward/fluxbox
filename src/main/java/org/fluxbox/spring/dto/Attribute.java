package org.fluxbox.spring.dto;

public class Attribute {
    private final String name;
    private final String value;
    private final String type;

    public Attribute(String name, String value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
    public static Attribute aStringAttribute(String name, String value ){
        return new Attribute(name,value,String.class.getSimpleName());
    }
}
