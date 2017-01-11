package ch.fhnw.ima.bimgur.client;

import ch.fhnw.ima.bimgur.activiti.ActivitiRestClient;
import ch.fhnw.ima.bimgur.activiti.model.User;
import ch.fhnw.ima.bimgur.activiti.service.IdentityService;
import ch.fhnw.ima.bimgur.client.login.LoginController;
import ch.fhnw.ima.bimgur.util.fx.notification.Notification;
import ch.fhnw.ima.bimgur.util.fx.notification.NotificationController;
import ch.fhnw.ima.bimgur.util.fx.notification.NotificationPopupDispatcher;
import io.reactivex.Single;
import javafx.application.Platform;
import javafx.stage.Stage;
import javaslang.control.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BimgurWorkController implements NotificationController, LoginController {

    private static final Logger LOG = LoggerFactory.getLogger(BimgurWorkController.class);
    private static final String ACTIVITI_REST_SERVICE_URL = "/activiti-rest/service/";

    private final BimgurWorkModel model;
    private final String baseUrl;

    BimgurWorkController(Stage popupOwner, BimgurWorkModel model) {
        this.model = model;
        this.baseUrl = model.getServerUrl() + ACTIVITI_REST_SERVICE_URL;

        model.getNotifications().addListener(new NotificationPopupDispatcher(popupOwner));
    }

    @Override
    public Single<User> login(String userId) {
        IdentityService identityService = ActivitiRestClient.connect(baseUrl, userId, userId).getIdentityService();
        Single<User> singleUser = identityService.getUser(userId);
        singleUser
                .doOnSubscribe(disposable -> startProgress())
                .doFinally(this::stopProgress)
                .subscribe(
                        validUser -> Platform.runLater(() -> model.currentUserProperty().setValue(Option.of(validUser))),
                        t -> notifyError("Login failed", t)
                );
        return singleUser;
    }

    void refresh() {
        // TODO: Implement
    }

    //------------------------------------------------
    // NotificationController
    //------------------------------------------------

    @Override
    public void notifyError(String message) {
        LOG.error(message);
        appendToModel(Notification.error(message));
    }

    @Override
    public void notifyError(String message, Throwable t) {
        LOG.error(message, t);
        appendToModel(Notification.error(message));
    }

    @Override
    public void notifyWarn(String message) {
        LOG.warn(message);
        appendToModel(Notification.warn(message));
    }

    @Override
    public void notifyInfo(String message) {
        LOG.info(message);
        appendToModel(Notification.info(message));
    }

    @Override
    public void notifySilent(String message) {
        LOG.debug(message);
        appendToModel(Notification.silent(message));
    }

    private void appendToModel(Notification notification) {
        Platform.runLater(() -> model.getNotifications().add(notification));
    }

    //------------------------------------------------
    // Progress Handling
    //------------------------------------------------

    private void startProgress() {
        updateProgress(1);
    }

    private void stopProgress() {
        updateProgress(-1);
    }

    private void updateProgress(int delta) {
        Platform.runLater(() ->
                model.concurrentTaskCountProperty().set(model.concurrentTaskCountProperty().get() + delta)
        );
    }

}