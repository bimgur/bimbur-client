package ch.fhnw.ima.bimgur.activiti.model;

import ch.fhnw.ima.bimgur.activiti.model.util.Id;

public final class UserId extends Id {

    public static final UserId NONE = new UserId(Id.NONE);

    public UserId(String raw) {
        super(raw);
    }

}