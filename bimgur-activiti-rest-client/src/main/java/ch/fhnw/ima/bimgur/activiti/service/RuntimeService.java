package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.model.ProcessInstance;
import ch.fhnw.ima.bimgur.activiti.model.StartProcessInstanceByIdDto;
import ch.fhnw.ima.bimgur.activiti.model.StartProcessInstanceByKeyDto;
import ch.fhnw.ima.bimgur.activiti.model.util.ResultList;
import ch.fhnw.ima.bimgur.activiti.service.util.ResultListExtractor;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RuntimeService {

    @GET("runtime/process-instances")
    Observable<ResultList<ProcessInstance>> getProcessInstancesResultList();

    default Observable<ProcessInstance> getProcessInstances() {
        return ResultListExtractor.extract(getProcessInstancesResultList());
    }

    @Headers("Content-type: application/json")
    @POST("runtime/process-instances")
    Observable<ProcessInstance> startProcessInstance(@Body StartProcessInstanceByKeyDto startData);

    @Headers("Content-type: application/json")
    @POST("runtime/process-instances")
    Observable<ProcessInstance> startProcessInstance(@Body StartProcessInstanceByIdDto startData);

}