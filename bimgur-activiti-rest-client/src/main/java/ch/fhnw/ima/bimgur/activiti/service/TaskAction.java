package ch.fhnw.ima.bimgur.activiti.service;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by fabrizio.parrillo on 22.12.2016.
 */
public class TaskAction {
        @JsonProperty("action")
        private final String ACTION = "complete";
}
