package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javaslang.Tuple2;
import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.Map;

public final class TaskFormData {

    private final String taskId;
    private final List<Map<String, String>> properties;

    @JsonCreator
    public TaskFormData(@JsonProperty("taskId") TaskId taskId, @JsonProperty("properties") List<Tuple2<FormPropertyId, String>> properties) {
        this.taskId = taskId.getRaw();
        this.properties = properties.map(t -> HashMap.of("id", t._1.getRaw(), "value", t._2));
    }

    public String getTaskId() {
        return taskId;
    }

    public List<Map<String, String>> getProperties() {
        return properties;
    }

}
