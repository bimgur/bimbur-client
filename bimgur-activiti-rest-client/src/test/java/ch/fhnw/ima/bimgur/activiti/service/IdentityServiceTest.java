package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.IntegrationTest;
import ch.fhnw.ima.bimgur.activiti.TestUtils;
import ch.fhnw.ima.bimgur.activiti.model.User;
import ch.fhnw.ima.bimgur.activiti.model.UserId;
import io.reactivex.Observable;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeAll;
<<<<<<< HEAD
=======
import org.junit.jupiter.api.Disabled;
>>>>>>> b959a3e42b2ea88ca7e6b65e62684d16db1846a7
import org.junit.jupiter.api.Test;

import java.io.IOException;

@IntegrationTest
@Disabled
public class IdentityServiceTest {

    private static IdentityService service;

    @BeforeAll
    static void beforeAll() {
        service = TestUtils.client().getIdentityService();
    }

    @Test
    void getUsers() {
        Observable<User> users = service.getUsers();
        users.map(User::getId).test()
                .assertResult(
                        new UserId("fozzie"),
                        new UserId("gonzo"),
                        new UserId("kermit")
                );
    }

    @Test
    void getUserExisting() {
        service.getUser("fozzie").map(User::getId).test()
                .assertResult(
                        new UserId("fozzie")
                );
    }

    @Test
    void getUserUnknown() throws IOException {
        service.getUser("unknown").map(User::getId).test()
                .assertError(t -> t.getMessage().startsWith("HTTP 404"));
    }

}
