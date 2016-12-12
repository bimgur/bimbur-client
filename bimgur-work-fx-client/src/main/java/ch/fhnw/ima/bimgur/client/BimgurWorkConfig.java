package ch.fhnw.ima.bimgur.client;

import com.typesafe.config.Config;

public final class BimgurWorkConfig {

    private static final String BIMGUR_WORK = "bimgur.work";
    private static final String BIMGUR_WORK_SERVER_URL = BIMGUR_WORK + ".server.url";

    private final String serverUrl;

    public BimgurWorkConfig(Config config) {
        serverUrl = config.getString(BIMGUR_WORK_SERVER_URL);
    }

    public String getServerUrl() {
        return serverUrl;
    }

}