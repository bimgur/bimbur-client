package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class FormProperty {

    private final FormPropertyId id;
    private final String value;
    private final String name;
    private final String type;

    @JsonCreator
    public FormProperty(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("type") String type,
            @JsonProperty("value") String value
    ) {
        this.id = new FormPropertyId(id);
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public FormPropertyId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

}
