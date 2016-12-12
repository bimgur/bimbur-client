package ch.fhnw.ima.bimgur.util.fx;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;

public class BusyOverlay extends StackPane {

    public BusyOverlay(ObservableValue<Boolean> isBusy) {
        visibleProperty().bind(isBusy);
        setStyle("-fx-background-color: rgba(200, 200, 200, .7)");
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxHeight(100);
        StackPane.setAlignment(progressIndicator, Pos.CENTER);
        getChildren().add(progressIndicator);
    }

}