package ch.fhnw.ima.bimgur.activiti;

public interface TestUtils {

    String BASE_URL = "http://192.168.99.100:8080/activiti-rest/service/";
    String USER_NAME = "kermit";
    String PASSWORD = "kermit";

    static ActivitiRestClient client() {
        return ActivitiRestClient.connect(BASE_URL, USER_NAME, PASSWORD);
    }

}
