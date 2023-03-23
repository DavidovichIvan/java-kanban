package Model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Task extends TemplateTask {

    private boolean isEpic;

    private String taskDescription;

    private List<SubTask> subTasksList;

    public Task() {
        super.taskCreationTime = Instant.now();
        super.taskStartTime = super.getTaskCreationTime();
        super.taskDuration = DEFAULT_DURATION_FOR_TASK_IN_MINUTES;
        super.taskEndTime = calculateTaskEndTime();

        super.taskId = idCounter;
        idCounter++;

        this.isEpic = false;
        this.name = "Имя задачи";
        this.taskDescription = "Пустая задача";
        this.taskStatus = TaskStatus.NEW;
        this.subTasksList = new ArrayList<>();
    }

    public Task(String taskName, String taskDescription) {
        super.taskCreationTime = Instant.now();
        super.taskStartTime = super.getTaskCreationTime();
        super.taskDuration = DEFAULT_DURATION_FOR_TASK_IN_MINUTES;
        super.taskEndTime = calculateTaskEndTime();

        super.taskId = idCounter;
        idCounter++;
        this.isEpic = false;

        this.name = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = TaskStatus.NEW;
        this.subTasksList = new ArrayList<>();
    }


    /**
     * getters/setters
     */
    public int getTaskId() {
        return taskId;
    }

    public List<SubTask> getSubTasksList() {
        return subTasksList;
    }

    public void setSubTasksList(List<SubTask> subTasksList) {
        this.subTasksList = subTasksList;
    }

    public boolean isEpic() {
        return isEpic;
    }

    public void setEpic(boolean epic) {
        isEpic = epic;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskDescription() {
        return taskDescription;
    }


    @Override
    public String toString() {
        return "Задача:{" +
                "ID задачи=" + taskId +
                ", Эпик=" + isEpic +
                ", Имя задачи='" + name + '\'' +
                ", Описание задачи='" + taskDescription + '\'' +
                ", Текущий статус=" + taskStatus +
                ", Время начала=" + taskStartTime +
                ", Время окончания=" + taskEndTime +
                ", Подзадачи:=" + subTasksList +
                '}';
    }

}