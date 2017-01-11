package ch.fhnw.ima.bimgur.client.task;

import ch.fhnw.ima.bimgur.activiti.model.User;
import ch.fhnw.ima.bimgur.client.model.RichTask;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public final class TaskTableView extends TableView<RichTask> {

    private static final String COLUMN_PROCESS = "Process";
    private static final String COLUMN_TASK = "Task";
    private static final String COLUMN_ASSIGNEE = "Assignee";

    public TaskTableView(ObservableList<RichTask> tasks) {
        super(tasks);

        TableColumn<RichTask, String> analysisNameColumn = new TableColumn<>(COLUMN_PROCESS);
        // TODO: Process name rather than id
        analysisNameColumn.setCellValueFactory(task -> new SimpleStringProperty(task.getValue().getProcessInstance().getId().getRaw()));

        TableColumn<RichTask, String> taskNameColumn = new TableColumn<>(COLUMN_TASK);
        taskNameColumn.setCellValueFactory(task -> new SimpleStringProperty(task.getValue().getName()));

        TableColumn<RichTask, User> assigneeColumn = new TableColumn<>(COLUMN_ASSIGNEE);
        assigneeColumn.setCellValueFactory(task -> new SimpleObjectProperty<>(task.getValue().getAssignee()));
        assigneeColumn.setCellFactory(column -> new UserCellValueRenderer<>());

        getColumns().add(analysisNameColumn);
        getColumns().add(taskNameColumn);
        getColumns().add(assigneeColumn);

        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private static class UserCellValueRenderer<T> extends TableCell<T, User> {

        private static final String UNASSIGNED = "Unassigned";

        @Override
        protected void updateItem(User user, boolean empty) {
            super.updateItem(user, empty);
            if (user == null || empty) {
                setText(null);
            } else {
                if (User.NONE.equals(user)) {
                    setText(UNASSIGNED);
                } else {
                    setText(user.getFirstName() + " " + user.getLastName());
                }
            }
        }

    }

}