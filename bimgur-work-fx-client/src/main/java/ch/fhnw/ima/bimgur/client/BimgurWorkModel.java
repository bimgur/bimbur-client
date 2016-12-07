package ch.fhnw.ima.bimgur.client;

import ch.fhnw.ima.bimgur.activiti.model.User;
import ch.fhnw.ima.bimgur.client.status.StatusBarPresentationModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javaslang.control.Option;

final class BimgurWorkModel implements StatusBarPresentationModel {

    private final ObjectProperty<Option<User>> currentUserProperty = new SimpleObjectProperty<>(Option.none());
    private final String serverUrl;

    BimgurWorkModel(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    ObjectProperty<Option<User>> currentUserProperty() {
        return currentUserProperty;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    @Override
    public String getStatusBarText() {
        return getServerUrl();
    }

}
