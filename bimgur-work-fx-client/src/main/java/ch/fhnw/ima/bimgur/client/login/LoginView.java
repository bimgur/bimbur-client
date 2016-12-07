package ch.fhnw.ima.bimgur.client.login;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class LoginView extends GridPane {

    public LoginView(LoginController controller) {
        Label label = new Label("User:");

        TextField textField = new TextField();

        Button button = new Button("Log In");
        button.setDefaultButton(true);
        button.setOnAction(e -> controller.login(textField.getText()));

        setAlignment(Pos.CENTER);
        setHgap(5);
        setVgap(5);

        add(label, 0, 0);
        add(textField, 1, 0);
        add(button, 1, 1);
        GridPane.setHalignment(button, HPos.RIGHT);

        Platform.runLater(textField::requestFocus);
    }

}
