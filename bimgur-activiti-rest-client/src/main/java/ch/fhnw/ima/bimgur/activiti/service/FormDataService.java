package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.model.*;
import io.reactivex.Observable;
import javaslang.Tuple2;
import javaslang.collection.List;
import okhttp3.ResponseBody;
import retrofit2.http.*;

public interface FormDataService {
    @GET("form/form-data")
    Observable<FormData> getFormDataListByTaskId(@Query("taskId") String taskId);

    default Observable<FormData> getTaskFormData(TaskId taskId) {
        return getFormDataListByTaskId(taskId.getRaw());
    }

    @GET("form/form-data")
    Observable<FormData> getFormDataListByProcessDefinitionId(@Query("processDefinitionId") String processDefinitionId);

    default Observable<FormData> getStartFormData(ProcessDefinitionId processDefinitionId) {
        return getFormDataListByProcessDefinitionId(processDefinitionId.getRaw());
    }

    @Headers("Content-type: application/json")
    @POST("form/form-data")
    Observable<ResponseBody> submitTaskFormData(@Body TaskFormData taskFormData);

    default Observable<ResponseBody> submitTaskFormData(TaskId taskId,
                                                        List<Tuple2<FormPropertyId, String>> properties) {
        return submitTaskFormData(new TaskFormData(taskId, properties));
    }
    @Headers("Content-type: application/json")
    @POST("form/form-data")
    Observable<ResponseBody> submitStartFormData(@Body ProcessDefinitionFormData processDefinitionFormData);

    default Observable<ResponseBody> submitStartFormData(ProcessDefinitionId processDefinitionId,
                                                         List<Tuple2<FormPropertyId, String>> properties) {
        return submitStartFormData(new ProcessDefinitionFormData(processDefinitionId, properties));
    }
}