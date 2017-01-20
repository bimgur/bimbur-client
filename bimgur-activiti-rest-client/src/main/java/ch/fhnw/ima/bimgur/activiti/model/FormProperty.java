package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class FormProperty {

    private final FormPropertyId id;
    private final String name;
    private final String type;
    private final String value;
    private final boolean writable;
    private final boolean required;
   // private final StringStringMap enumValues;

    @JsonCreator
    public FormProperty(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("type") String type,
            @JsonProperty("value") String value,
            @JsonProperty("writable") boolean writable,
            @JsonProperty("required") boolean required/*,
            @JsonProperty("enumValues") StringStringMap enumValues*/
    ) {
        this.id = new FormPropertyId(id);
        this.name = name;
        this.type = type;
        this.value = value;
        this.writable = writable;
        this.required = required;
       // this.enumValues = enumValues;
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

    public boolean isWritable() {
        return writable;
    }

    public boolean isRequired() {
        return required;
    }

  /*  @JsonDeserialize(as = StringStringMap.class)
    public Map<String, String> getEnumValues() {
        return ImmutableMap.copyOf(this.enumValues);
    }*/

}
