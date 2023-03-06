package Model;

import java.util.ArrayList;
import java.util.List;

public class Task extends TemplateTask {

    private boolean isEpic;


    private String taskDescription;

    private List<SubTask> subTasksList;

    public Task() {
        super.taskId = idCounter;
        idCounter++;

        this.isEpic = false;
        this.name = "Имя задачи";
        this.taskDescription = "Пустая задача";
        this.taskStatus = TaskStatus.NEW;
        this.subTasksList = new ArrayList<>();
    }

    public Task(String taskName, String taskDescription) {
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
                ", Подзадачи:=" + subTasksList +
                '}';
    }

}