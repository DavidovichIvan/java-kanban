package Model;

import java.util.ArrayList;

public class Epic extends TemplateTask {
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
        return "Эпик {" +
                "Основная задача эпика=" + epicTask +
                ", Подзадачи эпика=" + epicSubTasks +
                '}';
    }
}
