import java.util.Map;
public class Task {

    public String taskName;
    private String taskDescription;
    public int taskId;
    public boolean isEpic;
    int taskStatus;
    Map<Integer, String> taskStatusList = Map.ofEntries(
            Map.entry(1, "NEW"),
            Map.entry(2, "IN_PROGRESS"),
            Map.entry(3, "DONE"));

    public Task() {

        this.taskName = "Пустая задача";
        this.taskDescription = "Описание задачи отсутствует";
        this.isEpic = false;
        taskStatus = 1;
        taskId = DataManager.getId();
    }

    public Task(String taskName, String taskDescription, boolean isEpic, Integer taskStatus) {

        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.isEpic = isEpic;
        this.taskStatus = taskStatus;
        taskId = DataManager.getId();
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
