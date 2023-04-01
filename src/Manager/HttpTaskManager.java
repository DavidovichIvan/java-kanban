package Manager;

import HTTP.KVTaskClient;
import Interfaces.HistoryManager;
import Model.SubTask;
import Model.Task;
import Model.TemplateTask;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {

    private final String serverURL;

    private final String SERVER_KEY_FOR_TASKS = "tasksKey";
    private final String SERVER_KEY_FOR_HISTORY = "historyKey";

    KVTaskClient kvTaskClient;


    public HttpTaskManager(HistoryManager history, String dataPath, String apiToken) {
        super(history, dataPath);
        serverURL = dataPath;
        if (apiToken.isEmpty()) {
            apiToken = "API_TOKEN=DEBUG";
        }
        kvTaskClient = new KVTaskClient(apiToken, serverURL);

    }

    /**
     * auxiliary methods for saving data onto KVServer
     */

    private void saveTasksOnServer() {
        try {
            kvTaskClient.put(SERVER_KEY_FOR_TASKS, String.valueOf(allTasksList.values()));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveHistoryOnServer() {
        try {
            kvTaskClient.put(SERVER_KEY_FOR_HISTORY, String.valueOf(history));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    public void loadFromServer() {
       // Gson gson = new Gson();
            try {
         kvTaskClient.load(SERVER_KEY_FOR_TASKS); //так мы извлекаем строку json которую надо еще десериализовать и перезаписать в alltasksList с у казание в качестве ключа id задачи
         kvTaskClient.load(SERVER_KEY_FOR_HISTORY);   //так мы извлекаем строку json которую надо еще десериализовать и перезаписать в historylist
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
*/

    public void setAllTasksList(Map<Integer, Task> allTasksList) throws IOException, InterruptedException {
        super.setAllTasksList(allTasksList);
        saveTasksOnServer();
    }

    @Override
    public Task createNewTask() {
        Task t = super.createNewTask();
        saveTasksOnServer();
        return t;
    }

    @Override
    public Task createNewTask(String taskName, String taskDescription) {
        Task t = super.createNewTask(taskName, taskDescription);
        saveTasksOnServer();
        return t;
    }

    @Override
    public void createNewTask(Task task) {
        super.createNewTask(task);
        saveTasksOnServer();
    }

    @Override
    public SubTask createSubTask(int mainTaskId) {
        SubTask s = super.createSubTask(mainTaskId);
        saveTasksOnServer();
        return s;
    }

    @Override
    public SubTask createSubTask(int mainTaskId, String subTaskName, TemplateTask.TaskStatus taskStatus) {
        SubTask s = super.createSubTask(mainTaskId, subTaskName, taskStatus);
        saveTasksOnServer();
        return s;
    }

    @Override
    public Feedback createSubTask(int mainTaskId, SubTask subTask) {
        Feedback f = super.createSubTask(mainTaskId, subTask);
        saveTasksOnServer();
        return f;
    }

    @Override
    public Feedback updateSubTask(SubTask newSubTask, int subTaskID) {
        Feedback f = super.updateSubTask(newSubTask, subTaskID);
        saveTasksOnServer();
        return f;
    }

    @Override
    public Feedback changeTaskNameById(int taskID, String newTaskName) {
        Feedback f = super.changeTaskNameById(taskID, newTaskName);
        saveTasksOnServer();
        return f;
    }

    @Override
    public Feedback changeTaskDescriptionById(int taskID, String newTaskDescription) {
        Feedback f = super.changeTaskDescriptionById(taskID, newTaskDescription);
        saveTasksOnServer();
        return f;
    }

    @Override
    public Feedback changeNonEpicTaskStatusById(int taskID, TemplateTask.TaskStatus taskStatus) {
        Feedback f = super.changeNonEpicTaskStatusById(taskID, taskStatus);
        saveTasksOnServer();
        return f;
    }

    @Override
    public void updateTaskStatus(int taskID) {
        super.updateTaskStatus(taskID);
        saveTasksOnServer();
    }

    @Override
    public Feedback setTaskStartTime(int taskID, Instant startTime) {
        Feedback f = super.setTaskStartTime(taskID, startTime);
        saveTasksOnServer();
        return f;
    }

    @Override
    public Feedback setSubTaskStartTime(int taskID, int subId, Instant startTime) {
        Feedback f = super.setSubTaskStartTime(taskID, subId, startTime);
        saveTasksOnServer();
        return f;
    }

    @Override
    public Feedback setNonEpicTaskDuration(int taskID, long durationInMinutes) {
        Feedback f = super.setNonEpicTaskDuration(taskID, durationInMinutes);
        saveTasksOnServer();
        return f;
    }

    @Override
    public Feedback setSubTaskDuration(int taskID, int subId, long durationInMinutes) {
        Feedback f = super.setSubTaskDuration(taskID, subId, durationInMinutes);
        saveTasksOnServer();
        return f;
    }

    @Override
    public Feedback organizeSubTasksScheduleForSingleTask(int taskID) {
        Feedback f = super.organizeSubTasksScheduleForSingleTask(taskID);
        saveTasksOnServer();
        return f;
    }

    @Override
    public Feedback organizeScheduleForAllTasks() {
        Feedback f = super.organizeScheduleForAllTasks();
        saveTasksOnServer();
        return f;
    }

    @Override
    public Task getTaskById(int id) {
        Task t = super.getTaskById(id);
        saveHistoryOnServer();
        return t;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask s = super.getSubTaskById(id);
        saveHistoryOnServer();
        return s;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        saveTasksOnServer();
        saveHistoryOnServer();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        saveTasksOnServer();
        saveHistoryOnServer();
    }

    @Override
    public Feedback deleteSubTaskByID(int subTaskID) {
        Feedback f = super.deleteSubTaskByID(subTaskID);
        saveTasksOnServer();
        saveHistoryOnServer();
        return f;
    }

    @Override
    public Feedback deleteAllSubTasksByTaskId(int id) {
        Feedback f = super.deleteAllSubTasksByTaskId(id);
        saveTasksOnServer();
        saveHistoryOnServer();
        return f;
    }




}
