package ch.fhnw.ima.bimgur.client;

import ch.fhnw.ima.bimgur.activiti.model.User;
import ch.fhnw.ima.bimgur.client.login.LoginView;
import ch.fhnw.ima.bimgur.client.status.StatusBar;
import ch.fhnw.ima.bimgur.client.status.StatusBarController;
import ch.fhnw.ima.bimgur.util.fx.ToggleableControlsPane;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

class BimgurWorkView extends StackPane {

    private final BimgurWorkModel model;
    private final BimgurWorkController controller;
    private final StackPane centerPane;

    BimgurWorkView(BimgurWorkModel model, BimgurWorkController controller) {
        this.model = model;
        this.controller = controller;

        centerPane = new StackPane();

        model.currentUserProperty().addListener((observable, oldUser, newUser) -> {
            if (newUser.isDefined()) {
                String userFirstName = newUser.map(User::getFirstName).getOrElse("Stranger");
                setCenter(new Label("Welcome, " + userFirstName + "!"));
            }
        });

        getChildren().add(createViewContainer(centerPane));

        LoginView loginView = new LoginView(controller);
        setCenter(loginView);
    }

    private void setCenter(Node node) {
        centerPane.getChildren().setAll(node);
    }

    private Node createViewContainer(StackPane centerPane) {
        StackPane notificationList = new StackPane(new Label("Notification Log Coming Soon"));
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