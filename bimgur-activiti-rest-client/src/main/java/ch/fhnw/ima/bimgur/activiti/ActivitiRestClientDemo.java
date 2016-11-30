package ch.fhnw.ima.bimgur.activiti;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Observable;
import javaslang.collection.List;
import javaslang.jackson.datatype.JavaslangModule;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;

@SuppressWarnings("WeakerAccess")
public class ActivitiRestClientDemo {

    private static final String BASE_URL = "http://192.168.99.100:8080/activiti-rest/service/";
    private static final String USER_NAME = "kermit";
    private static final String PASSWORD = "kermit";

    private interface IdentityService {

        @GET("identity/users")
        Observable<ResultList<User>> getUsersResultList();

        default Observable<User> getUsers() {
            return ResultListExtractor.extract(getUsersResultList());
        }

    }

    private interface ResultListExtractor {
        static <T> Observable<T> extract(Observable<ResultList<T>> observableResultList) {
            return observableResultList.flatMap(resultList -> Observable.fromIterable(resultList.getData()));
        }
    }

    private static final class ResultList<T> {

        private final List<T> data;

        @JsonCreator
        public ResultList(@JsonProperty("data") List<T> data) {
            this.data = data;
        }

        public List<T> getData() {
            return data;
        }

    }

    private static final class User {

        private final String firstName;
        private final String lastName;

        @JsonCreator
        public User(@JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

    }

    public static void main(String... args) throws InterruptedException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaslangModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JacksonConverterFactory jacksonConverterFactory = JacksonConverterFactory.create(mapper);

        OkHttpClient client = new OkHttpClient.Builder()
                .authenticator((route, response) -> {
                    String credential = Credentials.basic(USER_NAME, PASSWORD);
                    return response.request().newBuilder()
                            .header("Authorization", credential)
                            .build();
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(jacksonConverterFactory)
                .client(client)
                .build();

        IdentityService identityService = retrofit.create(IdentityService.class);

        Observable<User> users = identityService.getUsers();
        users.subscribe(
                user -> System.out.println(user.getFirstName() + " " + user.getLastName()),
                Throwable::printStackTrace
        );
    }

}