import java.util.ArrayList;
public class Epic {
    Task epicTask;
    ArrayList<SubTask> epicSubTasks;

    @Override
    public String toString() {
        return "Epic{" +
                "EpicTask=" + epicTask +
                ", epicSubTasks=" + epicSubTasks +
                '}';
    }
}
