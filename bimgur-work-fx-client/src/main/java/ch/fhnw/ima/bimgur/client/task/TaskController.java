package ch.fhnw.ima.bimgur.client.task;

import ch.fhnw.ima.bimgur.client.model.RichTask;
import io.reactivex.Observable;

public interface TaskController {

    Observable<RichTask> loadTasks();

    Observable<RichTask> refreshTasks();

}