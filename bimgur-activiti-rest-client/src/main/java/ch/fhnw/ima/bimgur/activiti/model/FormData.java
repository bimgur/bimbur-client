package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javaslang.collection.List;

public final class FormData {

    private final String formKey;
    private final DeploymentId deploymentId;
    private final List<FormProperty> formProperties;

    @JsonCreator
    public FormData(
            @JsonProperty("formKey") String formKey,
            @JsonProperty("deploymentId") String deploymentId,
            @JsonProperty("formProperties") List<FormProperty> formProperties
    ) {
        this.formKey = formKey;
        this.deploymentId = new DeploymentId(deploymentId);
        this.formProperties = formProperties;
    }

    public String getFormKey() {
        return formKey;
    }

    public DeploymentId getDeploymentId() {
        return deploymentId;
    }

    public List<FormProperty> getFormProperties() {
        return formProperties;
    }

}
