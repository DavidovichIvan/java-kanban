package Manager;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;

public class Managers {


    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }


    public static TaskManager getManagerWithBackup(String dataPath) {
        return new FileBackedTasksManager(getDefaultHistory(), dataPath);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
