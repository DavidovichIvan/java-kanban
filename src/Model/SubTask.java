package Model;

public class SubTask extends TemplateTask {

    public SubTask() {
        super.taskId = idCounter;
        idCounter++;

        this.name = "Имя подзадачи";
        this.taskStatus = TaskStatus.NEW;
    }

    public SubTask(String subTaskName, TaskStatus taskStatus) {
        super.taskId = idCounter;
        idCounter++;

        this.name = subTaskName;
        this.taskStatus = taskStatus;
    }

    public int getSubTaskId() {
        return taskId;
    }

    public void setSubTaskId(int subTaskId) {
        this.taskId = subTaskId;
    }

    @Override
    public String toString() {
        return "Подзадача:{" +
                "ID подзадачи=" + taskId +
                ", имя подзадачи='" + name + '\'' +
                ", статус подзадачи=" + taskStatus +
                '}';
    }
}
