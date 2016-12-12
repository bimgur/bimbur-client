package ch.fhnw.ima.bimgur.client;

import ch.fhnw.ima.bimgur.activiti.model.User;
import ch.fhnw.ima.bimgur.client.status.StatusBarPresentationModel;
import ch.fhnw.ima.bimgur.util.fx.notification.Notification;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javaslang.control.Option;

final class BimgurWorkModel implements StatusBarPresentationModel {

    private final ObjectProperty<Option<User>> currentUserProperty = new SimpleObjectProperty<>(Option.none());
    private final String serverUrl;
    private final ObservableList<Notification> notifications;

    BimgurWorkModel(String serverUrl) {
        this.serverUrl = serverUrl;
        this.notifications = FXCollections.observableArrayList();
    }

    ObjectProperty<Option<User>> currentUserProperty() {
        return currentUserProperty;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public ObservableList<Notification> getNotifications() {
        return notifications;
    }

    @Override
    public String getStatusBarText() {
        return getServerUrl();
    }

}
