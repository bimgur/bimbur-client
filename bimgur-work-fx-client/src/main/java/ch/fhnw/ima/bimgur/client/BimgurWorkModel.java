package ch.fhnw.ima.bimgur.client;

import ch.fhnw.ima.bimgur.activiti.model.User;
import ch.fhnw.ima.bimgur.client.model.RichTask;
import ch.fhnw.ima.bimgur.client.status.StatusBarPresentationModel;
import ch.fhnw.ima.bimgur.util.fx.notification.Notification;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javaslang.control.Option;

final class BimgurWorkModel implements StatusBarPresentationModel {

    private final BimgurWorkConfig config;
    private final ObjectProperty<Option<User>> currentUserProperty = new SimpleObjectProperty<>(Option.none());
    private final ObservableList<Notification> notifications = FXCollections.observableArrayList();
    private final IntegerProperty concurrentOperationCount = new SimpleIntegerProperty();
    private final ObservableList<RichTask> tasks = FXCollections.observableArrayList();

    BimgurWorkModel(BimgurWorkConfig config) {
        this.config = config;
    }

    ObjectProperty<Option<User>> currentUserProperty() {
        return currentUserProperty;
    }

    public String getServerUrl() {
        return config.getServerUrl();
    }

    public ObservableList<Notification> getNotifications() {
        return notifications;
    }

    public ObservableList<RichTask> getTasks() {
        return tasks;
    }

    @Override
    public String getStatusBarText() {
        return getServerUrl();
    }

    public IntegerProperty concurrentOperationCountProperty() {
        return concurrentOperationCount;
    }

    public ObservableBooleanValue isBusy() {
        return concurrentOperationCount.greaterThan(0);
    }

}
