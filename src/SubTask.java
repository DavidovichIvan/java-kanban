public class SubTask {

    private static int subTaskIdCounter = 0;
    private Integer mainTaskId;
    private String subTaskName;
    private Integer subTaskStatus;

    private int subTaskId;

    public SubTask(Integer mainTaskId, String subTaskName, Integer subTaskStatus) {
        this.mainTaskId = mainTaskId;
        this.subTaskName = subTaskName;
        this.subTaskStatus = subTaskStatus;
        this.subTaskId = subTaskIdCounter;
        subTaskIdCounter++;
    }

    /**
     * getters/setters
     */
    public Integer getMainTaskId() {
        return mainTaskId;
    }

    public void setMainTaskId(Integer mainTaskId) {
        this.mainTaskId = mainTaskId;
    }

    public String getSubTaskName() {
        return subTaskName;
    }

    public void setSubTaskName(String subTaskName) {
        this.subTaskName = subTaskName;
    }

    public Integer getSubTaskStatus() {
        return subTaskStatus;
    }

    public void setSubTaskStatus(Integer subTaskStatus) {
        this.subTaskStatus = subTaskStatus;
    }

    public int getSubTaskId() {
        return subTaskId;
    }
    public void setSubTaskId(int subTaskId) {
        this.subTaskId = subTaskId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "subTaskName='" + subTaskName + '\'' +
                ", subTaskStatus=" + subTaskStatus +
                ", mainTaskId=" + mainTaskId +
                ", subTaskId=" + subTaskId +
                '}';
    }

}

