package ch.fhnw.ima.bimgur.activiti;

import ch.fhnw.ima.bimgur.activiti.service.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import javaslang.Lazy;
import javaslang.jackson.datatype.JavaslangModule;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@SuppressWarnings("WeakerAccess")
public final class ActivitiRestClient {

    private static final Logger LOG = LoggerFactory.getLogger(ActivitiRestClient.class);

    private final Lazy<IdentityService> identityService;
    private final Lazy<RepositoryService> repositoryServices;
    private final Lazy<RuntimeService> runtimeServices;
    private final Lazy<TaskService> taskService;
    private final Lazy<FormDataService> formDataService;

    private ActivitiRestClient(Retrofit retrofit) {
        this.identityService = Lazy.of(() -> retrofit.create(IdentityService.class));
        this.repositoryServices = Lazy.of(() -> retrofit.create(RepositoryService.class));
        this.runtimeServices = Lazy.of(() -> retrofit.create(RuntimeService.class));
        this.taskService = Lazy.of(() -> retrofit.create(TaskService.class));
        this.formDataService = Lazy.of(() -> retrofit.create(FormDataService.class));

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
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(LOG::debug);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder()
                .authenticator((route, response) -> {
                    String credential = Credentials.basic(username, password);

                    // If we already failed with these credentials, don't retry
                    // https://github.com/square/retrofit/issues/1561
                    if (credential.equals(response.request().header("Authorization"))) {
                        return null;
                    }

                    return response.request().newBuilder()
                            .header("Authorization", credential)
                            .build();
                })
                .addInterceptor(loggingInterceptor)
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

    public RepositoryService getRepositoryService() {
        return repositoryServices.get();
    }


    public RuntimeService getRuntimeService() {
        return this.runtimeServices.get();
    }

    public TaskService getTaskService() {
        return this.taskService.get();
    }

    public FormDataService getFormDataService() {
        return formDataService.get();
    }
}
