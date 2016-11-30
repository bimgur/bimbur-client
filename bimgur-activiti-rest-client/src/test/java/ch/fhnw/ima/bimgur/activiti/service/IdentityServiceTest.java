package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.IntegrationTest;
import ch.fhnw.ima.bimgur.activiti.TestUtils;
import ch.fhnw.ima.bimgur.activiti.model.User;
import ch.fhnw.ima.bimgur.activiti.model.UserId;
import io.reactivex.Observable;
import org.junit.jupiter.api.Test;

@IntegrationTest
public class IdentityServiceTest {

    @Test
    void getUsers() {
        Observable<User> users = TestUtils.client().getIdentityService().getUsers();
        users.map(User::getId).test().assertResult(
            new UserId("fozzie"),
            new UserId("gonzo"),
            new UserId("kermit")
        );
    }

}
