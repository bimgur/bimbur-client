package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.model.FormData;
import ch.fhnw.ima.bimgur.activiti.model.TaskId;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FormDataService {

    @GET("form/form-data")
    Observable<FormData> getFormDataList(@Query("taskId") String taskId);

    default Observable<FormData> getFrom(TaskId taskId) {
        return getFormDataList(taskId.getRaw());

    }




}