package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProcessDefinition {
    private final ProcessDefinitionId id;
    private final String key;
    private final String category;
    private final Boolean suspended;
    private final String name;

    @JsonCreator
    public ProcessDefinition(
            @JsonProperty("id") String id,
            @JsonProperty("key") String key,
            @JsonProperty("category") String category,
            @JsonProperty("suspended") Boolean suspended,
            @JsonProperty("name") String name) {
        this.id = new ProcessDefinitionId(id);
        this.key = key;
        this.category = category;
        this.suspended = suspended;
        this.name = name;
    }


    public ProcessDefinitionId getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getCategory() {
        return category;
    }

    public Boolean getSuspended() {
        return suspended;
    }

    public String getName() {
        return name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcessDefinition processDefinition = (ProcessDefinition) o;

        return this.id.equals(processDefinition.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + id;
    }
}
