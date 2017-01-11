package ch.fhnw.ima.bimgur.client;

import ch.fhnw.ima.bimgur.activiti.ActivitiRestClient;
import ch.fhnw.ima.bimgur.activiti.model.ProcessInstance;
import ch.fhnw.ima.bimgur.activiti.model.Task;
import ch.fhnw.ima.bimgur.activiti.model.User;
import ch.fhnw.ima.bimgur.activiti.model.UserId;
import ch.fhnw.ima.bimgur.activiti.service.IdentityService;
import ch.fhnw.ima.bimgur.client.login.LoginController;
import ch.fhnw.ima.bimgur.client.model.RichTask;
import ch.fhnw.ima.bimgur.client.task.TaskController;
import ch.fhnw.ima.bimgur.util.fx.notification.Notification;
import ch.fhnw.ima.bimgur.util.fx.notification.NotificationController;
import ch.fhnw.ima.bimgur.util.fx.notification.NotificationPopupDispatcher;
import io.reactivex.Observable;
import io.reactivex.Single;
import javafx.application.Platform;
import javafx.stage.Stage;
import javaslang.Lazy;
import javaslang.control.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BimgurWorkController implements
        NotificationController,
        LoginController,
        TaskController {

    private static final Logger LOG = LoggerFactory.getLogger(BimgurWorkController.class);
    private static final String ACTIVITI_REST_SERVICE_URL = "/activiti-rest/service/";

    private final BimgurWorkModel model;
    private final String baseUrl;
    private final Lazy<ActivitiRestClient> restClient;

    BimgurWorkController(Stage popupOwner, BimgurWorkModel model) {
        this.model = model;
        this.baseUrl = model.getServerUrl() + ACTIVITI_REST_SERVICE_URL;
        this.restClient = Lazy.of(() -> {
            User user = model.currentUserProperty().get().get();
            String userId = user.getId().getRaw();
            return ActivitiRestClient.connect(baseUrl, userId, userId);
        });

        model.getNotifications().addListener(new NotificationPopupDispatcher(popupOwner));
    }

    private ActivitiRestClient services() {
        return restClient.get();
    }

    void refresh() {
        refreshTasks();
    }

    //------------------------------------------------
    // NotificationController
    //------------------------------------------------

    @Override
    public Single<User> login(String userId) {
        IdentityService identityService = ActivitiRestClient.connect(baseUrl, userId, userId).getIdentityService();
        Single<User> singleUser = identityService.getUser(userId);
        singleUser
                .doOnSubscribe(disposable -> startProgress())
                .doFinally(this::stopProgress)
                .subscribe(
                        validUser -> Platform.runLater(() -> {
                            String msg = String.format("User '%s' logged in successfully", validUser.getId());
                            notifySilent(msg);
                            model.currentUserProperty().setValue(Option.of(validUser));
                        }),
                        t -> notifyError("Login failed", t)
                );
        return singleUser;
    }

    @Override
    public Observable<RichTask> loadTasks() {
        Observable<Task> tasks = services().getTaskService().getTasks();
        Observable<RichTask> richTasks = tasks.map(t -> {
                    // TODO: User non-blocking variants
                    User assignee = t.getAssigneeId().equals(UserId.NONE) ? User.NONE : services().getIdentityService().getUser(t.getAssigneeId().getRaw()).blockingGet();
                    ProcessInstance processInstance = services().getRuntimeService().getProcessInstances().filter(i -> i.getId().equals(t.getProcessInstanceId())).blockingFirst();
                    return new RichTask(t, assignee, processInstance);
                }
        );
        richTasks
                .doOnSubscribe(disposable -> startProgress())
                .doFinally(this::stopProgress)
                .subscribe(
                        loadedTask -> {
                            notifySilent("Loading tasks successful");
                            Platform.runLater(() -> model.getTasks().add(loadedTask));
                        },
                        t -> notifyError("Loading tasks failed", t)
                );
        return richTasks;
    }

    @Override
    public Observable<RichTask> refreshTasks() {
        model.getTasks().clear();
        return loadTasks();
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
                model.concurrentOperationCountProperty().set(model.concurrentOperationCountProperty().get() + delta)
        );
    }

}