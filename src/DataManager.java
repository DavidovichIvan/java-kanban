//Небольшой комментарий: я рассматривал разные варианты структуры организации и хранения данных.
//Основное из чего я в итоге исходил, что задача должна иметь возможность изменять статуc (эпик/не эпик),
//поэтому я добавил задачам признак isEpic, а их хранение осуществляется в единой для всех задач хэш-таблице с уникальным
//ключом-идентификатором, через который осуществляется основное взаимодействие с подзадачами.

//При таком подходе, нет необходимости иметь два хранилища данных для эпик и не эпик задач, а в случае изменения
//статуса задачи нет необходимости переписывать ее из одного хранилища в другое.

//При этом реализована возможность создания объектов класса Эпик, а также
//получения списка соответствующих объектов как для эпик задач так и для не эпик.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class DataManager {
    static ArrayList<Integer> existingIdList = new ArrayList<>();
    static HashMap<Integer, Task> allTasksList = new HashMap<>();
    public static ArrayList<SubTask> allSubTasksList = new ArrayList<>();

    /**
     * Method for creating unique ID for a new task
     */
    public static Integer getId() {

        Random random = new Random();
        //рандом установлен в диапазоне до 3 для удобства тестирования
        int id = random.nextInt(3);

        while (existingIdList.contains(id)) {
            id++;
        }
        existingIdList.add(id);
        return id;
    }

    /**
     * Method for creation a task with pre-defined parameters
     */
    public static Task createTask(String taskName,
                                  String taskDescription,
                                  boolean isEpic,
                                  Integer taskStatus) {

        Task task = new Task(taskName, taskDescription, isEpic, taskStatus);
        allTasksList.put(task.taskId, task);

        return task;
    }

    /**
     * Method for an empty task creation
     */
    public static Task createEmptyTask() {

        Task task = new Task();
        allTasksList.put(task.taskId, task);

        return task;
    }

    /**
     * Method for getting task by its Id
     */
    public static Task getTaskById(Integer id) {
        if (!allTasksList.containsKey(id)) {
            return null;
        } else {
            Task task = allTasksList.get(id);
            return task;
        }
    }

    /**
     * Method for getting task by its taskName
     */
    public static Task getTaskByName(String taskName) {

        for (Task task : allTasksList.values()) {
            if (task.taskName.equalsIgnoreCase(taskName)) {
                return task;
            }
        }
        return null;
    }

    /**
     * Method for updating existing task by its Id
     */
    public static String updateTaskById(Task task) {
        if (!allTasksList.containsKey(task.taskId)) {
            return Errors.error65;
        } else
            allTasksList.put(task.taskId, task);
        updateTaskStatus(task.taskId);
        return Errors.noError;
    }

    /**
     * Method for removing all tasks
     */
    public static void removeAllTasks() {
        allTasksList.clear();
        existingIdList.clear();
        allSubTasksList.clear();
    }

    /**
     * Method for removing task (and its subtasks) by task Id
     */
    public static String removeTaskById(Integer id) {
        if (!allTasksList.containsKey(id)) {
            return Errors.error65;
        }
        allTasksList.remove(id);
        existingIdList.remove(existingIdList.indexOf(id));
        deleteAllSubtasksByTaskId(id);

        return Errors.noError;

    }

    /**
     * Method for a subtask creation
     */
    public static SubTask createSubTask(int mainTaskId, String subTaskName, Integer subTaskStatus) {
        if (!existingIdList.contains(mainTaskId)) {
            return null;
        } else {
            SubTask subTask = new SubTask(mainTaskId, subTaskName, subTaskStatus);

            allSubTasksList.add(subTask);
            updateTaskStatus(mainTaskId);
            allTasksList.get(mainTaskId).isEpic = true;
            return subTask;
        }
    }

    /**
     * Method for getting subtask Id by its name
     */
    public static Integer getSubTaskIdByName(String subTaskName) {
        int index = 0;
        for (SubTask subTask : DataManager.allSubTasksList) {
            if (subTask.subTaskName.equalsIgnoreCase(subTaskName)) {
                return index;
            } else index++;
        }
        return null;
    }

    /**
     * Method for getting subTask name by its Id
     */
    public static String getSubTaskNameById(Integer id) {

        if (id >= 0 && id < allSubTasksList.size()) {
            return allSubTasksList.get(id).subTaskName;
        } else return Errors.error65;
    }

    /**
     * Method for getting all subtasks list for a certain task (by task id)
     */
    public static ArrayList<SubTask> getSubTasksListByTaskId(Integer id) {
        ArrayList<SubTask> subTasksListByTaskId = new ArrayList<>();

        for (SubTask subTask : allSubTasksList) {
            if (subTask.mainTaskId == id) {
                subTasksListByTaskId.add(subTask);
            }
        }
        return subTasksListByTaskId;
    }

    /**
     * Method for deleting all existing subtasks
     */
    public static void deleteAllSubtasks() {
        allSubTasksList.clear();

        for (Task task : allTasksList.values()) {
            task.isEpic = false;
        }
    }

    /**
     * Method for deleting all subtasks for a certain task by the task id
     */
    public static void deleteAllSubtasksByTaskId(Integer id) {
        Iterator<SubTask> ite = allSubTasksList.iterator();

        while (ite.hasNext()) {
            SubTask subTask = ite.next();
            if (subTask.mainTaskId == id)
                ite.remove();
        }
        allTasksList.get(id).isEpic = false;
    }

    /**
     * Method for deleting one certain subtask by its id
     */
    public static String deleteSubtaskByItsId(int id) {

        if (id >= 0 && id < allSubTasksList.size()) {

            int mainId = allSubTasksList.get(id).mainTaskId;
            allSubTasksList.remove(id);
            updateTaskStatus(mainId);

            allTasksList.get(mainId).isEpic = false;
            for (SubTask subTask : allSubTasksList) {
                if (subTask.mainTaskId == mainId) {
                    allTasksList.get(mainId).isEpic = true;
                }
            }
            return Errors.noError;
        } else return Errors.error65;
    }

    /**
     * Method for deleting one certain subtask by its name
     */
    public static String deleteSubtaskByItsName(String subTaskName) {
        int index = 0;

        for (SubTask subTask : allSubTasksList) {
            if (subTask.subTaskName.equalsIgnoreCase(subTaskName)) {
                int mainId = subTask.mainTaskId;
                allSubTasksList.remove(index);
                allTasksList.get(mainId).isEpic = false;

                for (SubTask subTask1 : allSubTasksList) {
                    if (subTask1.mainTaskId == mainId) {
                        allTasksList.get(mainId).isEpic = true;
                    }
                }
                updateTaskStatus(mainId);
                return Errors.noError;
            }
            index++;
        }
        return Errors.error77;
    }

    /**
     * Method for updating existing subtask
     */
    public static String updateSubTask(SubTask subTask) {
        int index = 0;

        for (SubTask sub : allSubTasksList) {
            if (sub.mainTaskId == subTask.mainTaskId &&
                    sub.subTaskName.equalsIgnoreCase(subTask.subTaskName)) {
                allSubTasksList.set(index, subTask);

                allTasksList.get(subTask.mainTaskId).isEpic = true;

                updateTaskStatus(sub.mainTaskId);
                return Errors.noError;
            }
            index++;
        }
        return Errors.error88;
    }

    /**
     * Method for getting an Epic object (task+related subtasks) by task id
     */
    public static Epic getEpicTaskById(Integer id) {
        Epic epic = new Epic();

        epic.epicTask = getTaskById(id);
        epic.epicSubTasks = getSubTasksListByTaskId(id);

        return epic;

    }

    /**
     * Method for getting list of Epic objects (without subtasks)
     */
    public static HashMap<Integer, Task> getAllEpicTasks() {
        HashMap<Integer, Task> allEpicTasksList = new HashMap<>();

        for (Task task : allTasksList.values()) {
            if (task.isEpic == true) {
                allEpicTasksList.put(task.taskId, task);
            }
        }
        return allEpicTasksList;
    }

    /**
     * Method for getting list of non-Epic objects
     */
    public static HashMap<Integer, Task> getAllNonEpicTasks() {
        HashMap<Integer, Task> allNonEpicTasksList = new HashMap<>();

        for (Task task : allTasksList.values()) {
            if (task.isEpic == false) {
                allNonEpicTasksList.put(task.taskId, task);
            }
        }
        return allNonEpicTasksList;
    }

    /**
     * Method for getting list of all Epic objects (tasks+subtasks)
     */
    public static ArrayList<Epic> getAllEpicTasksAndSubtasks() {
        ArrayList<Epic> allEpicsAndSubtasks = new ArrayList<>();

        for (Task task : allTasksList.values()) {
            Epic epic = new Epic();
            if (task.isEpic == true) {
                epic.epicTask = task;
                epic.epicSubTasks = getSubTasksListByTaskId(task.taskId);

                allEpicsAndSubtasks.add(epic);
            }

        }
        return allEpicsAndSubtasks;
    }

    /**
     * Method for updating task status (new/in progress/done) for a certain task
     */
    public static String updateTaskStatus(int id) {
        if (!existingIdList.contains(id)) {
            return Errors.error65;
        }

        ArrayList<SubTask> subTasksListByTaskId = getSubTasksListByTaskId(id);

        if (subTasksListByTaskId.isEmpty()) {
            return Errors.noError;
        } else
            allTasksList.get(id).taskStatus = 2;

        for (SubTask sub : subTasksListByTaskId) {
            if (sub.subTaskStatus == 2) {
                return Errors.noError;
            }
        }

        boolean isDone = true;
        for (SubTask sub : subTasksListByTaskId) {
            if (sub.subTaskStatus != 3) {
                isDone = false;
            }
        }
        if (isDone == true) {
            allTasksList.get(id).taskStatus = 3;
            return Errors.noError;
        } else if (isDone == false) {
            boolean isNew = true;
            for (SubTask sub : subTasksListByTaskId) {
                if (sub.subTaskStatus != 1) {
                    isNew = false;
                }
            }
            if (isNew == true) {
                allTasksList.get(id).taskStatus = 1;
                return Errors.noError;
            }
        }
        return Errors.noError;
    }
}
