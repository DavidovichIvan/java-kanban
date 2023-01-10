import java.util.ArrayList;

public class Epic {
    private Task epicTask;
    private ArrayList<SubTask> epicSubTasks;

    /**
     * getters/setters
     */
    public void setEpicTask(Task epicTask) {

        this.epicTask = epicTask;
    }

    public void setEpicSubTasks(ArrayList<SubTask> epicSubTasks) {

        this.epicSubTasks = epicSubTasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "EpicTask=" + epicTask +
                ", epicSubTasks=" + epicSubTasks +
                '}';
    }
}
