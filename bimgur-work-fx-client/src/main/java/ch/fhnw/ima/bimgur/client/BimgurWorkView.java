package ch.fhnw.ima.bimgur.client;

import ch.fhnw.ima.bimgur.client.login.LoginView;
import ch.fhnw.ima.bimgur.client.status.StatusBar;
import ch.fhnw.ima.bimgur.client.status.StatusBarController;
import ch.fhnw.ima.bimgur.client.task.TaskTableView;
import ch.fhnw.ima.bimgur.util.fx.ToggleableControlsPane;
import ch.fhnw.ima.bimgur.util.fx.notification.NotificationListView;
import ch.fhnw.ima.bimgur.util.fx.BusyOverlay;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

class BimgurWorkView extends StackPane {

    private final BimgurWorkModel model;
    private final BimgurWorkController controller;
    private final BusyOverlay progressOverlay;
    private final StackPane centerPane;

    BimgurWorkView(BimgurWorkModel model, BimgurWorkController controller) {
        this.model = model;
        this.controller = controller;
        this.progressOverlay = new BusyOverlay(model.isBusy());

        centerPane = new StackPane();

        model.currentUserProperty().addListener((observable, oldUser, newUser) -> {
            if (newUser.isDefined()) {
                setCenter(new TaskTableView(model.getTasks()));
                controller.loadTasks();
            }
        });

        getChildren().add(createViewContainer(centerPane));

        LoginView loginView = new LoginView(controller);
        setCenter(loginView);
    }

    private void setCenter(Node node) {
        centerPane.getChildren().setAll(node, progressOverlay);
    }

    private Node createViewContainer(StackPane centerPane) {
        NotificationListView notificationList = new NotificationListView(model.getNotifications());
        ToggleableControlsPane mainPane = new ToggleableControlsPane(centerPane, ToggleableControlsPane.ControlPanePosition.BOTTOM, notificationList);

        StatusBar statusBar = new StatusBar(model, new StatusBarController() {
            @Override
            public void toggleNotifications() {
                mainPane.toggleControlNode(notificationList);
            }

            @Override
            public void refresh() {
                controller.refresh();
            }
        });
        statusBar.setPadding(new Insets(5));

        VBox content = new VBox();
        VBox.setVgrow(mainPane, Priority.ALWAYS);
        VBox.setVgrow(statusBar, Priority.NEVER);
        content.getChildren().addAll(mainPane, statusBar);

        return content;
    }

}