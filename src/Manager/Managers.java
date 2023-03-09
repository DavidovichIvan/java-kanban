package Manager;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;

public class Managers {


    public static TaskManager getDefault() {

        // return new InMemoryTaskManager(getDefaultHistory());
        return InMemoryTaskManager.getManager();
    }


    public static TaskManager getManagerWithBackup(String dataPath) {
        //   return new FileBackedTasksManager(getDefaultHistory(), dataPath);
        return FileBackedTasksManager.getManager(dataPath);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
