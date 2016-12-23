package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by fabrizio.parrillo on 22.12.2016.
 */
public class TaskClaimDTO {
    @JsonProperty("action")
    private final String action = "claim";
    @JsonProperty("assignee")
    private final String userIdWhoClaims;

    public TaskClaimDTO(UserId userId) {
        this.userIdWhoClaims = userId.getRaw();
    }
}
