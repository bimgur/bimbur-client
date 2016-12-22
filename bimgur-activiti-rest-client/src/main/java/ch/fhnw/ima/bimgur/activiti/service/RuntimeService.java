package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.model.ProcessInstance;
import ch.fhnw.ima.bimgur.activiti.model.StartProcessInstanceById;
import ch.fhnw.ima.bimgur.activiti.model.StartProcessInstanceByKey;
import ch.fhnw.ima.bimgur.activiti.model.Task;
import ch.fhnw.ima.bimgur.activiti.model.util.ResultList;
import ch.fhnw.ima.bimgur.activiti.service.util.ResultListExtractor;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.*;

public interface RuntimeService {

    @GET("runtime/process-instances")
    Observable<ResultList<ProcessInstance>> getProcessInstancesResultList();

    default Observable<ProcessInstance> getProcessInstances() {
        return ResultListExtractor.extract(getProcessInstancesResultList());
    }

    @Headers("Content-type: application/json")
    @POST("runtime/process-instances")
    Observable<ProcessInstance> startProcessInstance(@Body StartProcessInstanceByKey startData);

    @Headers("Content-type: application/json")
    @POST("runtime/process-instances")
    Observable<ProcessInstance> startProcessInstance(@Body StartProcessInstanceById startData);

    @GET("runtime/tasks")
    Observable<ResultList<Task>> getTasksResultList();

    default Observable<Task> getTasks() {
        return ResultListExtractor.extract(getTasksResultList());
    }


    @Headers("Content-type: application/json")
    @POST("runtime/tasks/{taskId}")
    Observable<ResponseBody> complete(@Path("taskId")String  taskId, @Body TaskAction taskAction);
}