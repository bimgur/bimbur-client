package ch.fhnw.ima.bimgur.client;

import ch.fhnw.ima.bimgur.activiti.ActivitiRestClient;
import ch.fhnw.ima.bimgur.activiti.model.User;
import ch.fhnw.ima.bimgur.activiti.service.IdentityService;
import ch.fhnw.ima.bimgur.client.login.LoginController;
import javafx.application.Platform;
import javaslang.control.Option;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class BimgurWorkController implements LoginController {

    private static final Logger LOG = Logger.getLogger(BimgurWorkController.class.getSimpleName());
    private static final String BASE_URL = "http://192.168.99.100:8080/activiti-rest/service/";

    private final BimgurWorkModel model;

    BimgurWorkController(BimgurWorkModel model) {
        this.model = model;
    }

    @Override
    public void login(String userId) {
        IdentityService identityService = ActivitiRestClient.connect(BASE_URL, userId, userId).getIdentityService();
        Call<User> getUserCall = identityService.getUser(userId);
        getUserCall.enqueue((OnSuccessFxCallback<User>) validUser ->
                model.currentUserProperty().setValue(Option.of(validUser))
        );
    }

    /** Convenience implementation which returns successful results on the FX application thread. */
    private interface OnSuccessFxCallback<T> extends Callback<T> {

        void onSuccess(T result);

        @Override
        default void onResponse(Call<T> call, Response<T> response) {
            if (response.isSuccessful()) {
                LOG.log(Level.INFO, call.request().url() + " successful");
                Platform.runLater(() -> onSuccess(response.body()));
            } else {
                LOG.log(Level.INFO, call.request().url() + " unsuccessful");
            }
        }

        @Override
        default void onFailure(Call<T> call, Throwable t) {
            LOG.log(Level.SEVERE, call.request().url() + " failed", t);
        }

    }

}