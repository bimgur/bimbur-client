package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by fabrizio.parrillo on 23.12.2016.
 */
public class FormData {

    private final String formKey;
    private final DeploymentId deploymentId;
    private final List<FormProperty> formProperties;

    public FormData(
            @JsonProperty("formKey") String formKey,
            @JsonProperty("deploymentId") String deploymentId,
            @JsonProperty("formProperties")List<FormProperty> formProperties
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
