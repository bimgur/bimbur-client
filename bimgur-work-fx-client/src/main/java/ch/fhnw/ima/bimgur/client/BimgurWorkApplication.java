package ch.fhnw.ima.bimgur.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class BimgurWorkApplication extends Application {

    private static final Logger LOG = Logger.getLogger(BimgurWorkApplication.class.getSimpleName());

    @Override
    public void start(Stage stage) throws Exception {
        StackPane rootPane = new StackPane(new Label("Hello Bimgur"));
        Scene scene = new Scene(rootPane, 800, 600);

        stage.setOnCloseRequest(e -> {
            LOG.fine("Main window closed, exiting application");
            Platform.exit();
            System.exit(0);
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Exit VM if main frame is closed
        Platform.setImplicitExit(true);
        try {
            launch(args);
        } catch (Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            System.exit(-1);
        }
    }

}