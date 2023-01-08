public class SubTask {
    public Integer mainTaskId;
    public String subTaskName;
    public Integer subTaskStatus;

    public SubTask(Integer mainTaskId, String subTaskName, Integer subTaskStatus) {
        this.mainTaskId = mainTaskId;
        this.subTaskName = subTaskName;
        this.subTaskStatus = subTaskStatus;

    }

    @Override
    public String toString() {
        return "SubTask{" +
                "subTaskName='" + subTaskName + '\'' +
                ", subTaskStatus=" + subTaskStatus +
                ", mainTaskId=" + mainTaskId +
                '}';
    }

}

