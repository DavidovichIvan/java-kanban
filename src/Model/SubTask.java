package Model;

import java.time.Instant;

public class SubTask extends TemplateTask {

    public SubTask() {
        super.taskCreationTime = Instant.now();
        super.taskStartTime = super.getTaskCreationTime();
        super.taskDuration = DEFAULT_DURATION_FOR_SUBTASK_IN_MINUTES;
        super.taskEndTime = calculateTaskEndTime();

        super.taskId = idCounter;
        idCounter++;

        this.name = "Имя подзадачи";
        this.taskStatus = TaskStatus.NEW;
    }

    public SubTask(String subTaskName, TaskStatus taskStatus) {
        super.taskCreationTime = Instant.now();
        super.taskStartTime = super.getTaskCreationTime();
        super.taskDuration = DEFAULT_DURATION_FOR_SUBTASK_IN_MINUTES;
        super.taskEndTime = calculateTaskEndTime();

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
        return "SubTask:{" +
                "taskId=" + taskId +
                ", name='" + name + '\'' +
                ", taskStatus=" + taskStatus +
                ", taskStartTime=" + taskStartTime +
                ", taskEndTime=" + taskEndTime +
                '}';
    }
}
