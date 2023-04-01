package Interfaces;

import Manager.Feedback;
import Model.SubTask;
import Model.Task;
import Model.TemplateTask;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TaskManager {


    /**
     * methods for task creation
     */
    Task createNewTask();

    Task createNewTask(String taskName, String taskDescription);

    void createNewTask(Task task);

    /**
     * methods for subtask creation
     */

    SubTask createSubTask(int mainTaskId);

    SubTask createSubTask(int mainTaskId, String subTaskName, TemplateTask.TaskStatus taskStatus);

    Feedback createSubTask(int mainTaskId, SubTask subTask);

    /**
     * method for getting task by ID
     */
    Task getTaskById(int id);

    /**
     * method for getting subtask by subtask ID
     */
    SubTask getSubTaskById(int id);

    /**
     * method for getting all existing subtasks
     */
    List<SubTask> getAllExistingSubtasks();

    /**
     * method for getting all subtasks by task id
     */
    List<SubTask> getSubtasksForCertainTaskByID(int id);

    /**
     * method for getting all existing tasks
     */
    Map<Integer, Task> getAllTasksList();

    /**
     * method for getting all epic tasks
     */
    Map<Integer, Task> getAllEpicTasksList();

    /**
     * method for getting all non-epic tasks
     */
    Map<Integer, Task> getAllNonEpicTasksList();

    /**
     * method for deleting all tasks
     */
    void deleteAllTasks();

    /**
     * method for deleting task by ID
     */
    void deleteTaskById(int id);

    /**
     * method for deleting one certain subtask by its ID
     */
    Feedback deleteSubTaskByID(int subTaskID);

    /**
     * method for deleting all subtasks by task ID
     *
     * @return
     */
    Feedback deleteAllSubTasksByTaskId(int id);

    /**
     * method for updating subtask by its ID
     */
    Feedback updateSubTask(SubTask newSubTask, int subTaskID);

    /**
     * group of methods for updating task by its ID
     */

    Feedback changeTaskNameById(int taskID, String newTaskName);

    Feedback changeTaskDescriptionById(int taskID, String newTaskDescription);

    Feedback changeNonEpicTaskStatusById(int taskID, TemplateTask.TaskStatus taskStatus);

    /**
     * method for updating EpicTask status depending on subtasks' status
     */
    void updateTaskStatus(int taskID);


    /**
     * method for changing Task start time
     * * @return
     */
    Feedback setTaskStartTime(int taskID, Instant startTime);

    /**
     * method for changing SubTask start time
     * * @return
     */
    Feedback setSubTaskStartTime(int taskID, int subId, Instant startTime);

    /**
     * method for changing NonEpicTask Duration
     * * @return
     */
    Feedback setNonEpicTaskDuration(int taskID, long durationInMinutes);

    /**
     * method for changing SubTask Duration
     * * @return
     */
    Feedback setSubTaskDuration(int taskID, int subId, long durationInMinutes);

    /**
     * method for setting organized schedule for a certain Task
     * (all subtasks would be set one after another with start/end time correction if needed)
     * * @return
     */
    Feedback organizeSubTasksScheduleForSingleTask(int taskID);

    /**
     * method for setting organized schedule for all Tasks
     * (all subtasks within would be organized as well)
     * * @return
     */
    Feedback organizeScheduleForAllTasks();

    /**
     * Method for getting list of time-organized Tasks
     *
     * @return
     */
    List<Task> getPrioritizedTasks(HashMap<Integer, Task> allTasksList);

    HistoryManager getHistory();
    String getDataFilePath();
   void saveData();

}
