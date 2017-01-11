package ch.fhnw.ima.bimgur.client.login;

import ch.fhnw.ima.bimgur.activiti.model.User;
import io.reactivex.Single;

public interface LoginController {

    Single<User> login(String userId);

}
