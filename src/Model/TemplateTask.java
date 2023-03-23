package Model;

import java.time.Duration;
import java.time.Instant;

public abstract class TemplateTask {

    protected static int idCounter = 1;

    protected int taskId;


    protected String name;


    public enum TaskStatus {
        NEW,
        IN_PROGRESS,
        DONE
    }

    protected final Duration DEFAULT_DURATION_FOR_TASK_IN_MINUTES = Duration.ofMinutes(120);
    protected final Duration DEFAULT_DURATION_FOR_SUBTASK_IN_MINUTES = Duration.ofMinutes(20);
    protected TaskStatus taskStatus;

    protected Instant taskCreationTime;

    protected Instant taskStartTime;

    protected Duration taskDuration;

    protected Instant taskEndTime;

    public Instant getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(Instant taskEndTime) {
        this.taskEndTime = taskEndTime;
    }


    public Duration getTaskDuration() {
        return taskDuration;
    }

    public void setTaskDuration(Duration taskDuration) {
        this.taskDuration = taskDuration;
        this.taskEndTime = calculateTaskEndTime();
    }


    public Instant getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(Instant taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public void setTaskCreationTime(Instant taskCreationTime) {
        this.taskCreationTime = taskCreationTime;
    }

    public Instant getTaskCreationTime() {
        return taskCreationTime;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public Instant calculateTaskEndTime() {

    return taskStartTime.plus(taskDuration);

    }

}
