package ch.fhnw.ima.bimgur.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

public interface TestUtils {

    String HOST = "localhost"; // TODO: make configurable on OSX
    String BASE_URL = "http://" + HOST + ":8080/activiti-rest/service/";
    String USER_NAME = "kermit";
    String PASSWORD = "kermit";
    String DB_URL = "jdbc:postgresql://" + HOST + ":5433/bimgurdb";

    static ActivitiRestClient client() {
        return ActivitiRestClient.connect(BASE_URL, USER_NAME, PASSWORD);
    }

    static ProcessEngine processEngine() {
        return ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault()
                .setJdbcUrl(DB_URL)
                .buildProcessEngine();
    }

}
