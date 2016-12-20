package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by fabrizio.parrillo on 19.12.2016.
 */
public class Deployment {

    private final DeploymentId id;

    @JsonCreator
    public Deployment(@JsonProperty("id") String id) {
        this.id = new DeploymentId(id);

    }


    public DeploymentId getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deployment deployment = (Deployment) o;

        return this.id.equals(deployment.id);
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
