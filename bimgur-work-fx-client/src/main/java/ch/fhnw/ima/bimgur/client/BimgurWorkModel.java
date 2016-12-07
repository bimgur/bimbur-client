package ch.fhnw.ima.bimgur.client;

import ch.fhnw.ima.bimgur.activiti.model.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javaslang.control.Option;

final class BimgurWorkModel {

    private final ObjectProperty<Option<User>> currentUserProperty = new SimpleObjectProperty<>(Option.none());

    ObjectProperty<Option<User>> currentUserProperty() {
        return currentUserProperty;
    }

}
