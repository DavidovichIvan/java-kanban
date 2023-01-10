//Небольшой комментарий: я рассматривал разные варианты структуры организации и хранения данных.
//Основное из чего я в итоге исходил, что задача должна иметь возможность изменять статуc (эпик/не эпик),
//поэтому я добавил задачам признак isEpic, а их хранение осуществляется в единой для всех задач хэш-таблице с уникальным
//ключом-идентификатором, через который осуществляется основное взаимодействие с подзадачами.

//При таком подходе, нет необходимости иметь два хранилища данных для эпик и не эпик задач, а в случае изменения
//статуса задачи нет необходимости переписывать ее из одного хранилища в другое.

//При этом реализована возможность создания объектов класса Эпик, а также
//получения списка соответствующих объектов как для эпик задач так и для не эпик.

import java.util.*;

public class DataManager {
    private static final int NEW = 1;
    private static final int IN_PROGRESS = 2;
    private static final int DONE = 3;
    private ArrayList<Integer> existingIdList = new ArrayList<>();
    private HashMap<Integer, Task> allTasksList = new HashMap<>();
    private ArrayList<SubTask> allSubTasksList = new ArrayList<>();

    /**
     * getters/setters
     */
    public ArrayList<Integer> getExistingIdList() { return existingIdList; }
    public HashMap<Integer, Task> getAllTasksList() {
        return allTasksList;
    }

    public ArrayList<SubTask> getAllSubTasksList() {
        return allSubTasksList;
    }

    /**
     * Method for creation a task with pre-defined parameters
     */
    public Task createTask(String taskName,
                           String taskDescription,
                           boolean isEpic,
                           Integer taskStatus) {

        Task task = new Task(taskName, taskDescription, isEpic, taskStatus);
        allTasksList.put(task.getTaskId(), task);
        existingIdList.add(task.getTaskId());

        return task;
    }

    /**
     * Method for an empty task creation
     */
    public Task createEmptyTask() {

        Task task = new Task();
        allTasksList.put(task.getTaskId(), task);
        existingIdList.add(task.getTaskId());
        return task;
    }

    /**
     * Method for getting task by its Id
     */
    public Task getTaskById(Integer id) {
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
    public Task getTaskByName(String taskName) {

        for (Task task : allTasksList.values()) {
            if (task.getTaskName().equalsIgnoreCase(taskName)) {
                return task;
            }
        }
        return null;
    }

    /**
     * Method for updating existing task by its Id
     */
    public String updateTaskById(Task task, int taskId) {
        if (!allTasksList.containsKey(taskId)) {
            return Errors.nonExistingTaskId;
        } else {
            task.setTaskId(taskId);
            allTasksList.put(taskId, task);
            updateTaskStatus(task.getTaskId());
            return Errors.operationSuccessful;
        }
    }

    /**
     * Method for removing all tasks
     */
    public void removeAllTasks() {
        allTasksList.clear();
        existingIdList.clear();
        allSubTasksList.clear();
    }

    /**
     * Method for removing task (and its subtasks) by task Id
     */
    public String removeTaskById(Integer id) {
        if (!allTasksList.containsKey(id)) {
            return Errors.nonExistingTaskId;
        }
        allTasksList.remove(id);
        existingIdList.remove(existingIdList.indexOf(id));
        deleteAllSubtasksByTaskId(id);

        return Errors.operationSuccessful;

    }

    /**
     * Method for a subtask creation
     */
    public SubTask createSubTask(int mainTaskId, String subTaskName, Integer subTaskStatus) {
        if (!existingIdList.contains(mainTaskId)) {
            return null;
        } else {
            SubTask subTask = new SubTask(mainTaskId, subTaskName, subTaskStatus);

            allSubTasksList.add(subTask);
            updateTaskStatus(mainTaskId);
            allTasksList.get(mainTaskId).setEpic(true);
            return subTask;
        }
    }

    /**
     * Method for getting subtask Id by its name
     */
    public Integer getSubTaskIdByName(String subTaskName) {
        for (SubTask subTask : allSubTasksList) {
            if (subTask.getSubTaskName().equalsIgnoreCase(subTaskName)) {
                return subTask.getSubTaskId();
            }
        }
        return null;
    }

    /**
     * Method for getting subTask name by its Id
     */
    public String getSubTaskNameById(Integer id) {
        for (SubTask subTask : allSubTasksList) {
            if (subTask.getSubTaskId() == id) {
                return subTask.getSubTaskName();
            }
        }
        return Errors.nonExistingTaskId;
    }

    /**
     * Method for getting subtasks by its task id
     */
    public SubTask getSubTaskById(Integer id) {

        for (SubTask subTask : allSubTasksList) {
            if (subTask.getSubTaskId() == id) {
                return subTask;
            }
        }
        return null;
    }

    /**
     * Method for getting all subtasks list for a certain task (by task id)
     */
    public ArrayList<SubTask> getSubTasksListByTaskId(Integer id) {
        ArrayList<SubTask> subTasksListByTaskId = new ArrayList<>();

        for (SubTask subTask : allSubTasksList) {
            if (subTask.getMainTaskId() == id) {
                subTasksListByTaskId.add(subTask);
            }
        }
        return subTasksListByTaskId;
    }

    /**
     * Method for deleting all existing subtasks
     */
    public void deleteAllSubtasks() {
        allSubTasksList.clear();

        for (Task task : allTasksList.values()) {
            task.setEpic(false);
        }
    }

    /**
     * Method for deleting all subtasks for a certain task by the task id
     */
    public void deleteAllSubtasksByTaskId(Integer id) {
        Iterator<SubTask> ite = allSubTasksList.iterator();

        while (ite.hasNext()) {
            SubTask subTask = ite.next();
            if (subTask.getMainTaskId() == id) {
                ite.remove();
            }
        }
        allTasksList.get(id).setEpic(false);
    }

    /**
     * Method for deleting one certain subtask by its id
     */
    public String deleteSubtaskByItsId(Integer id) {
        int index = 0;
        for (SubTask sub : allSubTasksList) {
            if (sub.getSubTaskId() == id) {
                int mainId = sub.getMainTaskId();
                allSubTasksList.remove(index);
                updateTaskStatus(mainId);

                allTasksList.get(mainId).setEpic(false);
                for (SubTask subTask : allSubTasksList) {
                    if (subTask.getMainTaskId() == mainId) {
                        allTasksList.get(mainId).setEpic(true);
                    }
                }
                return Errors.operationSuccessful;
            }
            index++;
        }
        return Errors.noSubtaskFound;
    }

    /**
     * Method for updating existing subtask by subTaskId
     */
    public String updateSubTask(SubTask subTask, int subTaskId) {
        int index = 0;

        for (SubTask sub : allSubTasksList) {
            if (sub.getSubTaskId() == subTaskId && sub.getMainTaskId() == subTask.getMainTaskId()) {
                subTask.setSubTaskId(subTaskId);
                allSubTasksList.set(index, subTask);
                allTasksList.get(subTask.getMainTaskId()).setEpic(true);
                updateTaskStatus(sub.getMainTaskId());

                return Errors.operationSuccessful;
            }
            index++;
        }
        return Errors.noSubtaskFound;
    }

    /**
     * Method for getting an Epic object (task+related subtasks) by task id
     */
    public Epic getEpicTaskById(Integer id) {
        if (!getTaskById(id).isEpic()) {
            return null;
        }
        Epic epic = new Epic();
        epic.setEpicTask(getTaskById(id));
        epic.setEpicSubTasks(getSubTasksListByTaskId(id));

        return epic;
    }

    /**
     * Method for getting list of Epic objects (without subtasks)
     */
    public HashMap<Integer, Task> getAllEpicTasks() {
        HashMap<Integer, Task> allEpicTasksList = new HashMap<>();

        for (Task task : allTasksList.values()) {
            if (task.isEpic() == true) {
                allEpicTasksList.put(task.getTaskId(), task);
            }
        }
        return allEpicTasksList;
    }

    /**
     * Method for getting list of non-Epic objects
     */
    public HashMap<Integer, Task> getAllNonEpicTasks() {
        HashMap<Integer, Task> allNonEpicTasksList = new HashMap<>();

        for (Task task : allTasksList.values()) {
            if (task.isEpic() == false) {
                allNonEpicTasksList.put(task.getTaskId(), task);
            }
        }
        return allNonEpicTasksList;
    }

    /**
     * Method for getting list of all Epic objects (tasks+subtasks)
     */
    public ArrayList<Epic> getAllEpicTasksAndSubtasks() {
        ArrayList<Epic> allEpicsAndSubtasks = new ArrayList<>();

        for (Task task : allTasksList.values()) {
            Epic epic = new Epic();
            if (task.isEpic() == true) {

                epic.setEpicTask(task);
                epic.setEpicSubTasks(getSubTasksListByTaskId(task.getTaskId()));

                allEpicsAndSubtasks.add(epic);
            }
        }
        return allEpicsAndSubtasks;
    }

    /**
     * Method for updating task status (new/in progress/done) for a certain task
     */
    public String updateTaskStatus(int id) {
        if (!existingIdList.contains(id)) {
            return Errors.nonExistingTaskId;
        }
        ArrayList<SubTask> subTasksListByTaskId = getSubTasksListByTaskId(id);
        if (subTasksListByTaskId.isEmpty()) {
            allTasksList.get(id).setTaskStatus(NEW);
            return Errors.operationSuccessful;
        } else {
            allTasksList.get(id).setTaskStatus(IN_PROGRESS);

            for (SubTask sub : subTasksListByTaskId) {
                if (sub.getSubTaskStatus() == IN_PROGRESS) {
                    return Errors.operationSuccessful;
                }
            }
        }
        boolean isDone = true;
        for (SubTask sub : subTasksListByTaskId) {
            if (sub.getSubTaskStatus() != DONE) {
                isDone = false;
            }
        }
        if (isDone == true) {
            allTasksList.get(id).setTaskStatus(DONE);
            return Errors.operationSuccessful;
        } else if (isDone == false) {
            boolean isNew = true;
            for (SubTask sub : subTasksListByTaskId) {
                if (sub.getSubTaskStatus() != NEW) {
                    isNew = false;
                }
            }
            if (isNew == true) {
                allTasksList.get(id).setTaskStatus(NEW);
                return Errors.operationSuccessful;
            }
        }
        return Errors.operationSuccessful;
    }
}
