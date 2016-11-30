package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.model.User;
import ch.fhnw.ima.bimgur.activiti.model.util.ResultList;
import ch.fhnw.ima.bimgur.activiti.service.util.ResultListExtractor;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface IdentityService {

    @GET("identity/users")
    Observable<ResultList<User>> getUsersResultList();

    default Observable<User> getUsers() {
        return ResultListExtractor.extract(getUsersResultList());
    }

}