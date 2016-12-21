package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.model.ProcessInstance;
import ch.fhnw.ima.bimgur.activiti.model.Task;
import ch.fhnw.ima.bimgur.activiti.model.util.ResultList;
import ch.fhnw.ima.bimgur.activiti.service.util.ResultListExtractor;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RuntimeService {

    @GET("runtime/process-instances")
    Observable<ResultList<ProcessInstance>> getProcessInstancesResultList();

    default Observable<ProcessInstance> getProcessInstances() {
        return ResultListExtractor.extract(getProcessInstancesResultList());
    }


    @GET("runtime/tasks")
    Observable<ResultList<Task>> getTasksResultList();

    default Observable<Task> getTasks() {
        return ResultListExtractor.extract(getTasksResultList());
    }

    @FormUrlEncoded
    @POST("runtime/process-instances")
    Call<ProcessInstance> startProcessInstance(
            @Field("processDefinitionId") String processDefinitionId
    );

}