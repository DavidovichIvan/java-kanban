
package Manager;

import Interfaces.TaskManager;
import Model.Epic;
import Model.SubTask;
import Model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class InMemoryTaskManager implements TaskManager {
    private static final int NEW = 1;
    private static final int IN_PROGRESS = 2;
    private static final int DONE = 3;

    private ArrayList<Integer> existingIdList = new ArrayList<>();
    private HashMap<Integer, Task> allTasksList = new HashMap<>();
    private ArrayList<SubTask> allSubTasksList = new ArrayList<>();

    private InMemoryHistoryManager history = Managers.getDefaultHistory();

    /**
     * getters/setters
     */
    public ArrayList<Integer> getExistingIdList() {
        return existingIdList;
    }

    public HashMap<Integer, Task> getAllTasksList() {
        return allTasksList;
    }

    public InMemoryHistoryManager getHistory() {
        return history;
    }

    @Override
    public ArrayList<SubTask> getAllSubTasksList() {
        return allSubTasksList;
    }

    @Override
    public Task createTask(String taskName,
                           String taskDescription,
                           boolean isEpic,
                           Integer taskStatus) {

        Task task = new Task(taskName, taskDescription, isEpic, taskStatus);
        allTasksList.put(task.getTaskId(), task);
        existingIdList.add(task.getTaskId());

        return task;
    }

    @Override
    public Task createEmptyTask() {

        Task task = new Task();
        allTasksList.put(task.getTaskId(), task);
        existingIdList.add(task.getTaskId());
        return task;
    }

    @Override
    public Task getTaskById(Integer id) {
        if (!allTasksList.containsKey(id)) {
            return null;
        } else {
            Task task = allTasksList.get(id);

            if (task.isEpic()) {
                Epic epic = new Epic();
                epic.setEpicTask(getAllTasksList().get(id));
                epic.setEpicSubTasks(getSubTasksListByTaskId(id));
                history.updateHistoryList(epic);
            } else history.updateHistoryList(task);
            return task;
        }
    }

    @Override
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

    @Override
    public void removeAllTasks() {
        allTasksList.clear();
        existingIdList.clear();
        allSubTasksList.clear();
    }

    @Override
    public String removeTaskById(Integer id) {
        if (!allTasksList.containsKey(id)) {
            return Errors.nonExistingTaskId;
        }
        allTasksList.remove(id);
        existingIdList.remove(existingIdList.indexOf(id));
        deleteAllSubtasksByTaskId(id);

        return Errors.operationSuccessful;

    }

    @Override
    public SubTask createSubTask(int mainTaskId, String subTaskName, Integer subTaskStatus) {
        if (!allTasksList.containsKey(mainTaskId)) {
            return null;
        } else {
            SubTask subTask = new SubTask(mainTaskId, subTaskName, subTaskStatus);

            allSubTasksList.add(subTask);
            updateTaskStatus(mainTaskId);
            allTasksList.get(mainTaskId).setEpic(true);
            return subTask;
        }
    }

    @Override
    public Integer getSubTaskIdByName(String subTaskName) {
        for (SubTask subTask : allSubTasksList) {
            if (subTask.getSubTaskName().equalsIgnoreCase(subTaskName)) {
                return subTask.getSubTaskId();
            }
        }
        return null;
    }

    @Override
    public String getSubTaskNameById(Integer id) {
        for (SubTask subTask : allSubTasksList) {
            if (subTask.getSubTaskId() == id) {
                return subTask.getSubTaskName();
            }
        }
        return Errors.nonExistingTaskId;
    }

    @Override
    public SubTask getSubTaskById(Integer id) {

        for (SubTask subTask : allSubTasksList) {
            if (subTask.getSubTaskId() == id) {
                history.updateHistoryList(subTask);

                return subTask;
            }
        }
        return null;
    }

    @Override
    public ArrayList<SubTask> getSubTasksListByTaskId(Integer id) {
        ArrayList<SubTask> subTasksListByTaskId = new ArrayList<>();

        for (SubTask subTask : allSubTasksList) {
            if (subTask.getMainTaskId() == id) {
                subTasksListByTaskId.add(subTask);
            }
        }
        return subTasksListByTaskId;
    }

    @Override
    public void deleteAllSubtasks() {
        allSubTasksList.clear();

        for (Task task : allTasksList.values()) {
            task.setEpic(false);
        }
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public Epic getEpicTaskById(Integer id) {

        if (!getTaskById(id).isEpic()) {
            return null;
        }
        history.getHistoryList().remove(0);
        Epic epic = new Epic();
        epic.setEpicTask(getTaskById(id));
        epic.setEpicSubTasks(getSubTasksListByTaskId(id));
        history.getHistoryList().remove(0);

        history.updateHistoryList(epic);

        return epic;
    }

    @Override
    public HashMap<Integer, Task> getAllEpicTasks() {
        HashMap<Integer, Task> allEpicTasksList = new HashMap<>();

        for (Task task : allTasksList.values()) {
            if (task.isEpic() == true) {
                allEpicTasksList.put(task.getTaskId(), task);
            }
        }
        return allEpicTasksList;
    }

    @Override
    public HashMap<Integer, Task> getAllNonEpicTasks() {
        HashMap<Integer, Task> allNonEpicTasksList = new HashMap<>();

        for (Task task : allTasksList.values()) {
            if (task.isEpic() == false) {
                allNonEpicTasksList.put(task.getTaskId(), task);
            }
        }
        return allNonEpicTasksList;
    }

    @Override
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

    @Override
    public String updateTaskStatus(int id) {
        if (!allTasksList.containsKey(id)) {
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

    @Override
    public String toString() {
        return "InMemoryTaskManager{" +
                "existingIdList=" + existingIdList +
                ", allTasksList=" + allTasksList +
                ", allSubTasksList=" + allSubTasksList +
                '}';
    }
}
