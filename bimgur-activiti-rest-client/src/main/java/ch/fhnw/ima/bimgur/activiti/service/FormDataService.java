package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.model.FormData;
import ch.fhnw.ima.bimgur.activiti.model.FormDataByTaskIdDTO;
import ch.fhnw.ima.bimgur.activiti.model.TaskId;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.*;

public interface FormDataService {

    @GET("form/form-data")
    Observable<FormData> getFormDataList(@Query("taskId") String taskId);

    default Observable<FormData> getFrom(TaskId taskId) {
        return getFormDataList(taskId.getRaw());

    }

    @Headers("Content-type: application/json")
    @POST("form/form-data")
    Observable<ResponseBody> addFormData(@Body FormDataByTaskIdDTO formDataByTaskIdDTO);




}