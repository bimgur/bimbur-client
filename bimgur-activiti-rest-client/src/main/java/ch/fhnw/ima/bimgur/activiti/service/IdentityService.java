package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.model.User;
import ch.fhnw.ima.bimgur.activiti.model.util.ResultList;
import ch.fhnw.ima.bimgur.activiti.service.util.ResultListExtractor;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IdentityService {

    @GET("identity/users")
    Observable<ResultList<User>> getUsersResultList();

    @GET("identity/users/{userId}")
    Call<User> getUser(@Path("userId") String userId);

    default Observable<User> getUsers() {
        return ResultListExtractor.extract(getUsersResultList());
    }

}