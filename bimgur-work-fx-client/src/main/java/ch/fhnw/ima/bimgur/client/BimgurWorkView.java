package ch.fhnw.ima.bimgur.client;

import ch.fhnw.ima.bimgur.activiti.model.User;
import ch.fhnw.ima.bimgur.client.login.LoginView;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

class BimgurWorkView extends StackPane {

    BimgurWorkView(BimgurWorkModel model, BimgurWorkController controller) {
        LoginView loginView = new LoginView(controller);

        model.currentUserProperty().addListener((observable, oldUser, newUser) -> {
            // login successful?
            if (newUser.isDefined()) {
                String userFirstName = newUser.map(User::getFirstName).getOrElse("Stranger");
                getChildren().setAll(new Label("Welcome, " + userFirstName + "!"));
            }
        });

        getChildren().setAll(loginView);
    }

}