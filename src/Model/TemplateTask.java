package Model;

public abstract class TemplateTask {

    protected static int idCounter = 1;

    public int getTaskId() {
        return taskId;
    }

    protected int taskId;
    protected String name;

    public enum TaskStatus {
        NEW,
        IN_PROGRESS,
        DONE
    }

    protected TaskStatus taskStatus;

    public void setName(String name) {
        this.name = name;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

}
