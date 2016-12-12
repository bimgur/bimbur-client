package ch.fhnw.ima.bimgur.client;

import ch.fhnw.ima.bimgur.activiti.ActivitiRestClient;
import ch.fhnw.ima.bimgur.activiti.model.User;
import ch.fhnw.ima.bimgur.activiti.service.IdentityService;
import ch.fhnw.ima.bimgur.client.login.LoginController;
import ch.fhnw.ima.bimgur.util.fx.notification.Notification;
import ch.fhnw.ima.bimgur.util.fx.notification.NotificationController;
import ch.fhnw.ima.bimgur.util.fx.notification.NotificationPopupDispatcher;
import javafx.application.Platform;
import javafx.stage.Stage;
import javaslang.control.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.function.Consumer;

public final class BimgurWorkController implements LoginController, NotificationController {

    private static final Logger LOG = LoggerFactory.getLogger(BimgurWorkController.class);

    private final BimgurWorkModel model;
    private final String baseUrl;

    BimgurWorkController(Stage popupOwner, BimgurWorkModel model) {
        this.model = model;
        this.baseUrl = model.getServerUrl() + "/activiti-rest/service/";

        model.getNotifications().addListener(new NotificationPopupDispatcher(popupOwner));
    }

    @Override
    public void login(String userId) {
        IdentityService identityService = ActivitiRestClient.connect(baseUrl, userId, userId).getIdentityService();
        Call<User> getUserCall = identityService.getUser(userId);
        dispatch("Login", getUserCall, validUser ->
                model.currentUserProperty().setValue(Option.of(validUser))
        );
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

    /**
     * Dispatches the given call asynchronously:
     * - keeping track of progress
     * - logging success/failure with the given action description
     * - calling onSuccess on the FX application thread
     */
    private <T> void dispatch(String actionDesc, Call<T> call, Consumer<T> onSuccess) {

        Callback<T> delegate = new OnSuccessFxCallback<T>(actionDesc) {
            @Override
            void onSuccess(T result) {
                onSuccess.accept(result);
            }
        };

        startProgress();
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                stopProgress();
                delegate.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                stopProgress();
                delegate.onFailure(call, t);
            }
        });
    }

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

    /**
     * Convenience implementation which returns successful results on the FX application thread.
     */
    private abstract class OnSuccessFxCallback<T> implements Callback<T> {

        private final String actionDesc;

        private OnSuccessFxCallback(String actionDesc) {
            this.actionDesc = actionDesc;
        }

        abstract void onSuccess(T result);

        @Override
        public final void onResponse(Call<T> call, Response<T> response) {
            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                            notifySilent(actionDesc + " successful");
                            onSuccess(response.body());
                        }
                );
            } else {
                notifyError(actionDesc + " failed");
                logErrorUrl(call);
            }
        }

        @Override
        public final void onFailure(Call<T> call, Throwable t) {
            notifyError(actionDesc + " failed", t);
            logErrorUrl(call);
        }

        private void logErrorUrl(Call<T> call) {
            LOG.error(actionDesc + " URL: " + call.request().url());
        }

    }

}