//как я понял getDefault() это задел на будущее
// соответственно его реализация пока для единственного класса с интерфейсом TaskManager
package Manager;

import Interfaces.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        TaskManager taskManager = new InMemoryTaskManager();
        return taskManager;
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
