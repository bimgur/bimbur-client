package ch.fhnw.ima.bimgur.client;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BimgurWorkApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(BimgurWorkApplication.class);

    private BimgurWorkConfig config;

    @Override
    public void init() {
        try {
            // loads application.conf (with system properties mixed in)
            Config rawConfig = ConfigFactory.load();
            config = new BimgurWorkConfig(rawConfig);
            LOG.trace("Loaded config: " + rawConfig.root().render());
        } catch (Throwable t) {
            // Fail early --> log & exit
            LOG.error("Invalid/missing configuration", t);
            System.exit(-1);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        BimgurWorkModel model = new BimgurWorkModel(config);
        BimgurWorkController controller = new BimgurWorkController(stage, model);
        BimgurWorkView view = new BimgurWorkView(model, controller);
        Scene scene = new Scene(view, 800, 600);

        stage.setOnCloseRequest(e -> {
            LOG.trace("Main window closed, exiting application");
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
            LOG.error(t.getMessage(), t);
            System.exit(-1);
        }
    }

}