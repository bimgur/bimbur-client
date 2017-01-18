package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.engine.impl.form.LongFormType;
import org.activiti.engine.impl.form.StringFormType;

public final class FormProperty {

    private final FormPropertyId id;
    private final AbstractFormType modelValue;
    private final String formValue;
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
        this.formValue = value;
        this.type = type;

        switch (this.type) {
            case "long":
                this.modelValue = new LongFormType();
                break;

            default:
                this.modelValue = new StringFormType();

        }
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

    public AbstractFormType getModelValue() {
        return modelValue;
    }

    public String getFormValue() {
        return formValue;
    }
}
