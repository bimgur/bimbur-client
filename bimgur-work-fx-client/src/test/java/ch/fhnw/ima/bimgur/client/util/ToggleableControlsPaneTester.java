package ch.fhnw.ima.bimgur.client.util;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javaslang.collection.List;
import javaslang.collection.Stream;

public class ToggleableControlsPaneTester extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Pane centerPane = new VBox();
        centerPane.setStyle("-fx-background-color: darkblue");

        Stream<String> names = Stream.of("One", "Two", "Three");

        List<Pane> panes = names.map(this::createPane).toList();
        ToggleableControlsPane controlsPane = new ToggleableControlsPane(centerPane, ToggleableControlsPane.ControlPanePosition.LEFT, panes.toJavaArray(Pane.class));

        List<ToggleButton> buttons = names.zip(panes).map(t -> {
            ToggleButton b = new ToggleButton(t._1);
            b.setOnAction(e -> controlsPane.toggleControlNode(t._2));
            return b;
        }).toList();

        HBox buttonPane = new HBox(5);
        buttonPane.setPadding(new Insets(5, 5, 5, 5));
        buttonPane.getChildren().addAll(buttons.toJavaList());

        VBox root = new VBox(controlsPane, buttonPane);
        VBox.setVgrow(controlsPane, Priority.ALWAYS);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private Pane createPane(String text) {
        Pane pane = new StackPane(new Label(text));
        pane.setPrefHeight(200);
        return pane;
    }

    public static void main(String... args) {
        launch(args);
    }

}