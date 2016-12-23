package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.IntegrationTest;
import ch.fhnw.ima.bimgur.activiti.TestUtils;
import ch.fhnw.ima.bimgur.activiti.model.User;
import ch.fhnw.ima.bimgur.activiti.model.UserId;
import io.reactivex.Observable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class IdentityServiceTest {

    private static IdentityService service;

    @BeforeAll
    static void beforeAll() {
        service = TestUtils.client().getIdentityService();
    }

    @Test
    void getUsers() {
        Observable<User> users = service.getUsers();
        users.map(User::getId).test().assertResult(
            new UserId("fozzie"),
            new UserId("gonzo"),
            new UserId("kermit")
        );
    }

    @Test
    void getUserExisting() throws IOException {
        Response<User> response = service.getUser("fozzie").execute();
        assertTrue(response.isSuccessful());
        assertEquals(new UserId("fozzie"), response.body().getId());
    }

    @Test
    void getUserUnknown() throws IOException {
        Response<User> response = service.getUser("unknown").execute();
        assertFalse(response.isSuccessful());
    }

}
