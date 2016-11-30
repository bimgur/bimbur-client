package ch.fhnw.ima.bimgur.activiti;

import ch.fhnw.ima.bimgur.activiti.service.IdentityService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import javaslang.Lazy;
import javaslang.jackson.datatype.JavaslangModule;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@SuppressWarnings("WeakerAccess")
public final class ActivitiRestClient {

    private final Lazy<IdentityService> identityService;

    private ActivitiRestClient(Retrofit retrofit) {
        this.identityService = Lazy.of(() -> retrofit.create(IdentityService.class));
    }

    public static ActivitiRestClient connect(String baseUrl, String username, String password) {
        JacksonConverterFactory jacksonConverterFactory = createJacksonConverterFactory();
        OkHttpClient httpClient = createOkHttpClient(username, password);
        Retrofit retrofit = createRetrofit(baseUrl, jacksonConverterFactory, httpClient);
        return new ActivitiRestClient(retrofit);
    }

    public IdentityService getIdentityService() {
        return identityService.get();
    }

    private static JacksonConverterFactory createJacksonConverterFactory() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaslangModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return JacksonConverterFactory.create(mapper);
    }

    private static OkHttpClient createOkHttpClient(String username, String password) {
        return new OkHttpClient.Builder()
                .authenticator((route, response) -> {
                    String credential = Credentials.basic(username, password);
                    return response.request().newBuilder()
                            .header("Authorization", credential)
                            .build();
                })
                .build();
    }

    private static Retrofit createRetrofit(String baseUrl, JacksonConverterFactory jacksonConverterFactory, OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(jacksonConverterFactory)
                .client(httpClient)
                .build();
    }

}
