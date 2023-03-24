
package Manager;

import Exceptions.ManagerSaveException;
import Interfaces.HistoryManager;
import Model.SubTask;
import Model.Task;
import Model.TemplateTask;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Manager.Managers.getDefaultHistory;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String dataFilePath;
    private final String historyFilePath;

    private final String DATA_STORAGE_FILE_NAME = "Data.csv";
    private final String HISTORY_STORAGE_FILE_NAME = "History.csv";


    public FileBackedTasksManager(HistoryManager history, String dataPath) {
        super(history);
        this.dataFilePath = dataPath + File.separator + DATA_STORAGE_FILE_NAME;
        this.historyFilePath = dataPath + File.separator + HISTORY_STORAGE_FILE_NAME;

    }

    public String getDataFilePath() {
        return dataFilePath;
    }


    public void setAllTasksList(Map<Integer, Task> allTasksList) {
        super.setAllTasksList(allTasksList);
        saveData();
    }


    @Override
    public Task createNewTask() {
        Task t = super.createNewTask();
        saveData();
        return t;
    }

    @Override
    public Task createNewTask(String taskName, String taskDescription) {
        Task t = super.createNewTask(taskName, taskDescription);
        saveData();
        return t;
    }

    @Override
    public void createNewTask(Task task) {
        super.createNewTask(task);
        saveData();
    }

    @Override
    public SubTask createSubTask(int mainTaskId) {
        SubTask s = super.createSubTask(mainTaskId);
        saveData();
        return s;
    }

    @Override
    public SubTask createSubTask(int mainTaskId, String subTaskName, TemplateTask.TaskStatus taskStatus) {
        SubTask s = super.createSubTask(mainTaskId, subTaskName, taskStatus);
        saveData();
        return s;
    }

    @Override
    public Feedback createSubTask(int mainTaskId, SubTask subTask) {
        Feedback f = super.createSubTask(mainTaskId, subTask);
        saveData();
        return f;
    }

    @Override
    public Task getTaskById(int id) {
        Task t = super.getTaskById(id);
        saveHistory();
        return t;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask s = super.getSubTaskById(id);
        saveHistory();
        return s;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        saveData();
        saveHistory();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        saveData();
        saveHistory();
    }

    @Override
    public Feedback deleteSubTaskByID(int subTaskID) {
        Feedback f = super.deleteSubTaskByID(subTaskID);
        saveData();
        saveHistory();
        return f;
    }

    @Override
    public Feedback deleteAllSubTasksByTaskId(int id) {
        Feedback f = super.deleteAllSubTasksByTaskId(id);
        saveData();
        saveHistory();
        return f;
    }

    @Override
    public Feedback updateSubTask(SubTask newSubTask, int subTaskID) {
        Feedback f = super.updateSubTask(newSubTask, subTaskID);
        saveData();
        return f;
    }

    @Override
    public Feedback changeTaskNameById(int taskID, String newTaskName) {
        Feedback f = super.changeTaskNameById(taskID, newTaskName);
        saveData();
        return f;
    }

    @Override
    public Feedback changeTaskDescriptionById(int taskID, String newTaskDescription) {
        Feedback f = super.changeTaskDescriptionById(taskID, newTaskDescription);
        saveData();
        return f;
    }

    @Override
    public Feedback changeNonEpicTaskStatusById(int taskID, TemplateTask.TaskStatus taskStatus) {
        Feedback f = super.changeNonEpicTaskStatusById(taskID, taskStatus);
        saveData();
        return f;
    }

    @Override
    public void updateTaskStatus(int taskID) {
        super.updateTaskStatus(taskID);
        saveData();
    }

    //------------------------------------------new
    @Override
    public Feedback setTaskStartTime(int taskID, Instant startTime) {
        Feedback f = super.setTaskStartTime(taskID, startTime);
        saveData();
        return f;
    }

    @Override
    public Feedback setSubTaskStartTime(int taskID, int subId, Instant startTime) {
        Feedback f = super.setSubTaskStartTime(taskID, subId, startTime);
        saveData();
        return f;
    }

    @Override
    public Feedback setNonEpicTaskDuration(int taskID, long durationInMinutes) {
        Feedback f = super.setNonEpicTaskDuration(taskID, durationInMinutes);
        saveData();
        return f;

    }

    @Override
    public Feedback setSubTaskDuration(int taskID, int subId, long durationInMinutes) {
        Feedback f = super.setSubTaskDuration(taskID, subId, durationInMinutes);
        saveData();
        return f;
    }

    @Override
    public Feedback organizeSubTasksScheduleForSingleTask(int taskID) {
        Feedback f = super.organizeSubTasksScheduleForSingleTask(taskID);
        saveData();
        return f;
    }

    @Override
    public Feedback organizeScheduleForAllTasks() {
        Feedback f = super.organizeScheduleForAllTasks();
        saveData();
        return f;
    }

    @Override
    public List<Task> getPrioritizedTasks(HashMap<Integer, Task> allTasksList) {
        return super.getPrioritizedTasks(allTasksList);
    }

    /**
     * Auxiliary method for creating file or clearing up an existing one
     *
     * @param dataPath = full path to .csv files
     */
    public void setFileForDataStorage(String dataPath) throws ManagerSaveException {
        try {
            Files.createFile(Path.of(dataPath));
        } catch (IOException e) {
            try (FileWriter fileWr = new FileWriter(dataPath, StandardCharsets.UTF_8)) {
                fileWr.write("");
            } catch (IOException ex) {
                throw new ManagerSaveException(dataFilePath);
            }
        }
    }

    /**
     * Auxiliary method for a SubTask to be represented as a string
     */
    public String subTaskToString(SubTask s) {
        return (s.getSubTaskId() + ","
                + s.getName() + ","
                + s.getTaskStatus() + ","
                + s.getTaskCreationTime() + ","
                + s.getTaskStartTime() + ","
                + s.getTaskDuration() + ","
                + s.getTaskEndTime()
        );
    }

    /**
     * Auxiliary method for writing a Task to a certain file
     */
    public void saveTask(Task t, String dataPath) throws ManagerSaveException {
        try (FileWriter fileWriter = new FileWriter(dataPath, StandardCharsets.UTF_8, true)) {

            fileWriter.write(t.getTaskId() + "," +
                    t.isEpic() + "," +
                    t.getName() + "," +
                    t.getTaskDescription() + "," +
                    t.getTaskStatus() + "," +
                    t.getTaskCreationTime() + "," +
                    t.getTaskStartTime() + "," +
                    t.getTaskDuration() + "," +
                    t.getTaskEndTime()
            );

            if (!t.isEpic()) {
                fileWriter.write("\n");
            } else if (t.isEpic()) {
                fileWriter.write(",,,");
                for (SubTask s : t.getSubTasksList()) {
                    fileWriter.write(subTaskToString(s));
                    fileWriter.write(",");
                }
                fileWriter.write("\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException(dataFilePath);
        }

    }

    /**
     * Method for auto updating Tasks/SubTasks data in files
     */
    private void saveData() {
        setFileForDataStorage(dataFilePath);
        for (Task t : allTasksList.values()) {
            saveTask(t, dataFilePath);
        }
    }

    /**
     * Method for auto updating history of views data in files
     */
    private void saveHistory() {
        setFileForDataStorage(historyFilePath);
        for (TemplateTask t : getHistory().getHistoryList()) {
            if (t instanceof Task) {
                saveTask((Task) t, historyFilePath);
            } else if (t instanceof SubTask) {
                try (FileWriter fileWriter = new FileWriter(historyFilePath, StandardCharsets.UTF_8, true)) {
                    fileWriter.write(",,," + subTaskToString((SubTask) t) + "\n");
                } catch (IOException e) {
                    throw new ManagerSaveException(historyFilePath);
                }
            }
        }
    }

    /**
     * Method for restoring TasksManager from files with pre-saved data
     */
    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager loadedFile = new FileBackedTasksManager(getDefaultHistory(), file.toString());

        List<String> listOfTasks;
        listOfTasks = Files.readAllLines(Path.of(loadedFile.dataFilePath));

        for (String str : listOfTasks) {
            Task task = loadTaskFromFile(str);
            loadedFile.allTasksList.put(task.getTaskId(), task);
        }

        List<String> listOfHistory;
        listOfHistory = Files.readAllLines(Path.of(loadedFile.historyFilePath));
        int index = 0;
        for (String str : listOfHistory) {

            if (!str.substring(0, 3).contains(",,,")) {
                loadedFile.getHistory().getHistoryMap().put(index, loadTaskFromFile(str));
                index++;
            } else if (str.substring(0, 3).contains(",,,")) {
                str = str.substring(3);
                loadedFile.getHistory().getHistoryMap().put(index, loadSubTaskFromFile(str));
                index++;
            }
        }

        return loadedFile;
    }

    /**
     * Auxiliary method for creation a Task during loading from file
     */

    public static Task loadTaskFromFile(String str) {

        String[] lineContents = str.split(",,,");
        String[] taskByStrings = lineContents[0].split(",");

        Task task = new Task();
        task.setTaskId(Integer.parseInt(taskByStrings[0]));
        task.setEpic(Boolean.parseBoolean(taskByStrings[1]));
        task.setName(taskByStrings[2]);
        task.setTaskDescription(taskByStrings[3]);
        task.setTaskStatus(TemplateTask.TaskStatus.valueOf(taskByStrings[4]));
        task.setTaskCreationTime(Instant.parse(taskByStrings[5]));
        task.setTaskStartTime(Instant.parse(taskByStrings[6]));
        task.setTaskDuration(Duration.parse(taskByStrings[7]));
        task.setTaskEndTime(Instant.parse(taskByStrings[8]));

        if (lineContents.length == 2) {
            String[] AllSubTasksInStrings = lineContents[1].split(",,");
            for (String sub : AllSubTasksInStrings) {
                SubTask subTask = loadSubTaskFromFile(sub);
                task.getSubTasksList().add(subTask);
            }
        }

        return task;
    }

    /**
     * Auxiliary method for creation a SubTask during loading from file
     */
    public static SubTask loadSubTaskFromFile(String str) {
        String[] subTaskInStrings = str.split(",");
        SubTask subTask = new SubTask();
        subTask.setSubTaskId(Integer.parseInt(subTaskInStrings[0]));
        subTask.setName(subTaskInStrings[1]);
        subTask.setTaskStatus(TemplateTask.TaskStatus.valueOf(subTaskInStrings[2]));
        subTask.setTaskCreationTime(Instant.parse(subTaskInStrings[3]));
        subTask.setTaskStartTime(Instant.parse(subTaskInStrings[4]));
        subTask.setTaskDuration(Duration.parse(subTaskInStrings[5]));
        subTask.setTaskEndTime(Instant.parse(subTaskInStrings[6]));
        return subTask;
    }
}

//Спасибо за работу по ревью.