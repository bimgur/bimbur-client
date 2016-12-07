package ch.fhnw.ima.bimgur.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class BimgurWorkApplication extends Application {

    private static final Logger LOG = Logger.getLogger(BimgurWorkApplication.class.getSimpleName());
    private static final String SERVER_URL = "http://192.168.99.100:8080";

    @Override
    public void start(Stage stage) throws Exception {
        BimgurWorkModel model = new BimgurWorkModel(SERVER_URL);
        BimgurWorkController controller = new BimgurWorkController(model);
        BimgurWorkView view = new BimgurWorkView(model, controller);
        Scene scene = new Scene(view, 800, 600);

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