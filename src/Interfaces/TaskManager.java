package Interfaces;

import Manager.Feedback;
import Model.SubTask;
import Model.Task;
import Model.TemplateTask;

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
     */
    void deleteAllSubTasksByTaskId(int id);

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

}
