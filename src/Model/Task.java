package Model;

import java.util.Map;

public class Task extends TemplateTask {
    private static int idCounter = 0;
    private String taskName;
    private String taskDescription;
    private int taskId;
    private boolean isEpic;
    private int taskStatus;
    private Map<Integer, String> taskStatusList = Map.ofEntries(
            Map.entry(1, "НОВАЯ"),
            Map.entry(2, "ВЫПОЛНЯЕТСЯ"),
            Map.entry(3, "ЗАВЕРШЕНА"));

    public Task() {

        this.taskName = "Пустая задача";
        this.taskDescription = "Описание задачи отсутствует";
        this.isEpic = false;
        taskStatus = 1;
        taskId = idCounter;
        idCounter++;
    }

    public Task(String taskName, String taskDescription, boolean isEpic, Integer taskStatus) {

        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.isEpic = isEpic;
        this.taskStatus = taskStatus;
        taskId = idCounter;
        idCounter++;
    }

    /**
     * getters/setters
     */
    public String getTaskName() {
        return taskName;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public boolean isEpic() {
        return isEpic;
    }

    public void setEpic(boolean epic) {
        isEpic = epic;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        String subTask = "";
        if (isEpic == true) {
            subTask = "Эпик";
        } else subTask = "Не эпик";

        return
                "Номер задачи: " + taskId
                        + ". Задача: " + taskName
                        + ". Описание задачи: " + taskDescription
                        + ". Статус задачи: " + taskStatusList.get(taskStatus)
                        + ". Вид задачи: " + subTask;
    }
}
