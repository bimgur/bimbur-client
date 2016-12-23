package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.model.*;
import ch.fhnw.ima.bimgur.activiti.model.util.ResultList;
import ch.fhnw.ima.bimgur.activiti.service.util.ResultListExtractor;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.*;

public interface TaskService {

    @GET("runtime/tasks")
    Observable<ResultList<Task>> getTasksResultList(@Query("assignee") String assigneeUserId,
                                                    @Query("processInstanceId") String processInstaneId,
                                                    @Query("candidateUser") String candidateUserId);

    default Observable<Task> getTasks() {
        return ResultListExtractor.extract(
                getTasksResultList(
                        null,
                        null,
                        null
                ));
    }

    default Observable<Task> getFilteredTasks(UserId assignee,
                                              ProcessInstanceId processInstanceId,
                                              UserId candidate) {
        return ResultListExtractor.extract(
                getTasksResultList(
                        (assignee == null) ? null : assignee.getRaw(),
                        (processInstanceId == null) ? null : processInstanceId.getRaw(),
                        (candidate == null) ? null : candidate.getRaw()
                ));
    }


    default Observable<Task> getTasksByAssignee(UserId assignee) {
        return ResultListExtractor
                .extract(getTasksResultList(
                        assignee.getRaw(),
                        null,
                        null
                ));
    }

    default Observable<Task> getTasksByProcessInstanceId(ProcessInstanceId processInstanceId) {
        return ResultListExtractor
                .extract(getTasksResultList(
                        null,
                        processInstanceId.getRaw(),
                        null
                ));
    }

    default Observable<Task> getTasksByCandidateUser(UserId candidateUser) {
        return ResultListExtractor
                .extract(getTasksResultList(
                        null,
                        null,
                        candidateUser.getRaw()
                ));
    }


    /**
     * Called when the task is successfully executed.
     *
     * @param taskId          the id of the task to complete, cannot be null.
     * @param taskCompleteDTO the complete data transfer object, cannot be null.
     */
    @Headers("Content-type: application/json")
    @POST("runtime/tasks/{taskId}")
    Observable<ResponseBody> complete(@Path("taskId") String taskId, @Body TaskCompleteDTO taskCompleteDTO);

    /**
     * Claim responsibility for a task: the given user is made assignee for the task.
     * The difference with {@link # setAssignee(String, String)} is that here
     * a check is done if the task already has a user assigned to it.
     * No check is done whether the user is known by the identity component.
     *
     * @param taskId          task to claim, cannot be null.
     * @param taskCompleteDTO user that claims the task. When userId is null the task is unclaimed,
     *                        assigned to no one.
     */
    @Headers("Content-type: application/json")
    @POST("runtime/tasks/{taskId}")
    Observable<ResponseBody> claim(@Path("taskId") String taskId, @Body TaskClaimDTO taskCompleteDTO);

}