package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProcessInstance {

    private final ProcessInstanceId id;
    private final String url;
    private final String activityId;
    private final String processDefinitionUrl;
    private final Boolean suspended;
    private final String businessKey;

    @JsonCreator
    public ProcessInstance(
            @JsonProperty("id") String id,
            @JsonProperty("url") String url,
            @JsonProperty("businessKey") String businessKey,
            @JsonProperty("suspended") Boolean suspended,
            @JsonProperty("processDefinitionUrl") String processDefinitionUrl,
            @JsonProperty("activityId") String activityId
    ) {
        this.id = new ProcessInstanceId(id);
        this.url = url;
        this.businessKey = businessKey;
        this.suspended = suspended;
        this.processDefinitionUrl = processDefinitionUrl;
        this.activityId = activityId;
    }

    public ProcessInstanceId getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getProcessDefinitionUrl() {
        return processDefinitionUrl;
    }

    public Boolean getSuspended() {
        return suspended;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcessInstance processInstance = (ProcessInstance) o;

        return this.id.equals(processInstance.id);
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
