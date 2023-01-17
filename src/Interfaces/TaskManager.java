package Interfaces;

import Model.Epic;
import Model.SubTask;
import Model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    public ArrayList<SubTask> getAllSubTasksList();

    /**
     * Method for creation a task with pre-defined parameters
     */
    Task createTask(String taskName,
                    String taskDescription,
                    boolean isEpic,
                    Integer taskStatus);

    /**
     * Method for an empty task creation
     */
    Task createEmptyTask();

    /**
     * Method for getting task by its Id
     */
    Task getTaskById(Integer id);

    /**
     * Method for updating existing task by its Id
     */
    String updateTaskById(Task task, int taskId);

    /**
     * Method for removing all tasks
     */
    void removeAllTasks();

    /**
     * Method for removing task (and its subtasks) by task Id
     */
    String removeTaskById(Integer id);

    /**
     * Method for a subtask creation
     */
    SubTask createSubTask(int mainTaskId, String subTaskName, Integer subTaskStatus);

    /**
     * Method for getting subtask Id by its name
     */
    Integer getSubTaskIdByName(String subTaskName);

    /**
     * Method for getting subTask name by its Id
     */
    String getSubTaskNameById(Integer id);

    /**
     * Method for getting subtasks by its task id
     */
    SubTask getSubTaskById(Integer id);

    /**
     * Method for getting all subtasks list for a certain task (by task id)
     */
    ArrayList<SubTask> getSubTasksListByTaskId(Integer id);

    /**
     * Method for deleting all existing subtasks
     */
    void deleteAllSubtasks();

    /**
     * Method for deleting all subtasks for a certain task by the task id
     */
    void deleteAllSubtasksByTaskId(Integer id);

    /**
     * Method for deleting one certain subtask by its id
     */
    String deleteSubtaskByItsId(Integer id);

    /**
     * Method for updating existing subtask by subTaskId
     */
    String updateSubTask(SubTask subTask, int subTaskId);

    /**
     * Method for getting an Epic object (task+related subtasks) by task id
     */
    Epic getEpicTaskById(Integer id);

    /**
     * Method for getting list of Model.Epic objects (without subtasks)
     */
    HashMap<Integer, Task> getAllEpicTasks();

    /**
     * Method for getting list of non-Model.Epic objects
     */
    HashMap<Integer, Task> getAllNonEpicTasks();

    /**
     * Method for getting list of all Model.Epic objects (tasks+subtasks)
     */
    ArrayList<Epic> getAllEpicTasksAndSubtasks();

    /**
     * Method for updating task status (new/in progress/done) for a certain task
     */
    String updateTaskStatus(int id);

}

