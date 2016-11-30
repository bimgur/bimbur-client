package ch.fhnw.ima.bimgur.activiti.model.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javaslang.collection.List;

public final class ResultList<T> {

    private final List<T> data;

    @JsonCreator
    public ResultList(@JsonProperty("data") List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

}