package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by fabrizio.parrillo on 23.12.2016.
 */
public class FormProperty {

    private String value;
    private String fromPropertyId;
    private String name;
    private String type;

    public void FromProperty(
            @JsonProperty("id") String fromPropertyId,
            @JsonProperty("name") String name,
            @JsonProperty("type") String type,
            @JsonProperty("value") String value
    ) {
        this.fromPropertyId = fromPropertyId;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getFromPropertyId() {
        return fromPropertyId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
