package Manager;

import HTTP.KVTaskClient;
import Interfaces.HistoryManager;
import Interfaces.TaskManager;

import java.io.IOException;

public class Managers {

    private static final String DEFAULT_URL = "http://localhost:8078/";
    private static final String DEFAULT_API_TOKEN = "API_TOKEN=DEBUG";

    public static TaskManager getDefault() {
        return new HttpTaskManager(getDefaultHistory(), new KVTaskClient(DEFAULT_API_TOKEN, DEFAULT_URL));
    }


   // public static TaskManager getDefault() throws IOException, InterruptedException {
   //     return new HttpTaskManager(getDefaultHistory(), DEFAULT_URL, DEFAULT_API_TOKEN);
   //       }

    public static TaskManager getDefaultOld() {
        return new InMemoryTaskManager(getDefaultHistory());
   }


    public static TaskManager getManagerWithBackup(String dataPath) {
        return new FileBackedTasksManager(getDefaultHistory(), dataPath);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}

