package Manager;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import Model.SubTask;
import Model.Task;
import Model.TemplateTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private Map<Integer, Task> allTasksList = new HashMap<>();

    private InMemoryHistoryManager history;

    public InMemoryTaskManager(HistoryManager history) {
        this.history = (InMemoryHistoryManager) history;
    }

    public InMemoryHistoryManager getHistory() {
        return history;
    }

    @Override
    public Task createNewTask() {
        Task task = new Task();
        allTasksList.put(task.getTaskId(), task);
        return task;
    }

    @Override
    public Task createNewTask(String taskName, String taskDescription) {
        Task task = new Task(taskName, taskDescription);
        allTasksList.put(task.getTaskId(), task);
        return task;
    }

    @Override
    public void createNewTask(Task task) {
        allTasksList.put(task.getTaskId(), task);
    }

    @Override
    public SubTask createSubTask(int mainTaskId) {
        if (!allTasksList.containsKey(mainTaskId)) {
            return null;
        } else {
            SubTask subTask = new SubTask();
            getAllTasksList().get(mainTaskId).getSubTasksList().add(subTask);
            updateTaskStatus(mainTaskId);
            if (!getAllTasksList().get(mainTaskId).getSubTasksList().isEmpty()) {
                getAllTasksList().get(mainTaskId).setEpic(true);
            }
            return subTask;
        }
    }

    @Override
    public SubTask createSubTask(int mainTaskId, String subTaskName, TemplateTask.TaskStatus taskStatus) {
        if (!allTasksList.containsKey(mainTaskId)) {
            return null;
        } else {
            SubTask subTask = new SubTask(subTaskName, taskStatus);
            getAllTasksList().get(mainTaskId).getSubTasksList().add(subTask);
            updateTaskStatus(mainTaskId);
            if (!getAllTasksList().get(mainTaskId).getSubTasksList().isEmpty()) {
                getAllTasksList().get(mainTaskId).setEpic(true);
            }
            return subTask;
        }
    }

    @Override
    public Feedback createSubTask(int mainTaskId, SubTask subTask) {
        if (!allTasksList.containsKey(mainTaskId)) {
            return Feedback.NON_EXISTING_TASK_ID;
        } else {
            getAllTasksList().get(mainTaskId).getSubTasksList().add(subTask);
            updateTaskStatus(mainTaskId);
            if (!getAllTasksList().get(mainTaskId).getSubTasksList().isEmpty()) {
                getAllTasksList().get(mainTaskId).setEpic(true);
            }
            return Feedback.SUBTASK_SUCCESSFULLY_ADDED;
        }
    }

    @Override
    public Task getTaskById(int id) {

        history.updateHistoryList(allTasksList.get(id));

        return allTasksList.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        for (Task task : allTasksList.values()) {
            List<SubTask> subTasksList = task.getSubTasksList();

            for (SubTask subTask : subTasksList) {
                if (subTask.getSubTaskId() == id) {
                    history.updateHistoryList(subTask);
                    return subTask;
                }
            }
        }
        return null;
    }

    @Override
    public List<SubTask> getAllExistingSubtasks() {
        List<SubTask> allSubTasksList = new ArrayList<>();
        for (Task task : allTasksList.values()) {
            allSubTasksList.addAll(task.getSubTasksList());
        }
        return allSubTasksList;
    }

    @Override
    public List<SubTask> getSubtasksForCertainTaskByID(int id) {
        return getTaskById(id).getSubTasksList();
    }

    @Override
    public Map<Integer, Task> getAllTasksList() {
        return allTasksList;
    }

    @Override
    public Map<Integer, Task> getAllEpicTasksList() {
        Map<Integer, Task> epicTasksList = new HashMap<>();
        for (Task task : allTasksList.values()) {
            if (task.isEpic()) {
                epicTasksList.put(task.getTaskId(), task);
            }
        }
        return epicTasksList;
    }

    @Override
    public Map<Integer, Task> getAllNonEpicTasksList() {
        Map<Integer, Task> nonEpicTasksList = new HashMap<>();
        for (Task task : allTasksList.values()) {
            if (!task.isEpic()) {
                nonEpicTasksList.put(task.getTaskId(), task);
            }
        }
        return nonEpicTasksList;
    }

    @Override
    public void deleteAllTasks() {
        allTasksList.clear();
        history.getHistoryMap().clear();
    }

    @Override
    public void deleteTaskById(int id) {

        allTasksList.remove(id);
        history.removeFromHistory(id);
    }

    @Override
    public Feedback deleteSubTaskByID(int subTaskID) {

        for (Task task : getAllTasksList().values()) {
            int index = 0;
            for (SubTask sub : task.getSubTasksList()) {
                if (sub.getSubTaskId() == subTaskID) {
                    task.getSubTasksList().remove(index);

                    if (task.getSubTasksList().isEmpty()) {
                        getTaskById(task.getTaskId()).setEpic(false);
                    }

                    updateTaskStatus(task.getTaskId());
                    history.removeFromHistory(subTaskID);
                    return Feedback.SUBTASK_SUCCESSFULLY_DELETED;
                }
                index++;
            }
        }

        return Feedback.FAILED_TO_DELETE_SUBTASK_NON_EXISTING_ID;
    }

    @Override
    public void deleteAllSubTasksByTaskId(int id) {

        for (SubTask sub : getAllTasksList().get(id).getSubTasksList()) {
            history.removeFromHistory(sub.getSubTaskId());
        }

        getAllTasksList().get(id).getSubTasksList().clear();
        getAllTasksList().get(id).setEpic(false);
    }

    @Override
    public Feedback updateSubTask(SubTask newSubTask, int subTaskID) {
        newSubTask.setSubTaskId(subTaskID);

        for (Task task : getAllTasksList().values()) {
            int index = 0;
            for (SubTask sub : task.getSubTasksList()) {
                if (sub.getSubTaskId() == subTaskID) {
                    task.getSubTasksList().set(index, newSubTask);
                    getTaskById(task.getTaskId()).setEpic(true);
                    updateTaskStatus(task.getTaskId());

                    return Feedback.SUBTASK_SUCCESSFULLY_UPDATED;
                }
                index++;
            }
        }
        return Feedback.FAILED_TO_UPDATE_SUBTASK_NON_EXISTING_ID;
    }

    @Override
    public Feedback changeTaskNameById(int taskID, String newTaskName) {
        if (allTasksList.containsKey(taskID)) {
            allTasksList.get(taskID).setName(newTaskName);
            return Feedback.TASK_SUCCESSFULLY_UPDATED;
        }
        return Feedback.NON_EXISTING_TASK_ID;
    }

    @Override
    public Feedback changeTaskDescriptionById(int taskID, String newTaskDescription) {
        if (allTasksList.containsKey(taskID)) {
            allTasksList.get(taskID).setTaskDescription(newTaskDescription);
            return Feedback.TASK_SUCCESSFULLY_UPDATED;
        }
        return Feedback.NON_EXISTING_TASK_ID;
    }

    @Override
    public Feedback changeNonEpicTaskStatusById(int taskID, TemplateTask.TaskStatus taskStatus) {
        if (!allTasksList.containsKey(taskID)) {
            return Feedback.NON_EXISTING_TASK_ID;
        }
        if (allTasksList.get(taskID).isEpic()) {
            return Feedback.UNABLE_TO_UPDATE_STATUS_FOR_EPIC_TASK;
        }
        allTasksList.get(taskID).setTaskStatus(taskStatus);
        return Feedback.TASK_SUCCESSFULLY_UPDATED;
    }

    @Override
    public void updateTaskStatus(int taskID) {
        List<SubTask> subTasksList = getTaskById(taskID).getSubTasksList();

        for (SubTask sub : subTasksList) {
            if (sub.getTaskStatus() == TemplateTask.TaskStatus.IN_PROGRESS) {
                getTaskById(taskID).setTaskStatus(TemplateTask.TaskStatus.IN_PROGRESS);
                return;
            }
        }
        boolean isNew = true;
        for (SubTask sub : subTasksList) {
            if (sub.getTaskStatus() != TemplateTask.TaskStatus.NEW) {
                isNew = false;
            }
        }
        if (isNew) {
            getTaskById(taskID).setTaskStatus(TemplateTask.TaskStatus.NEW);
            return;
        }
        boolean isDone = true;
        for (SubTask sub : subTasksList) {
            if (sub.getTaskStatus() != TemplateTask.TaskStatus.DONE) {
                isDone = false;
            }
        }
        if (isDone) {
            getTaskById(taskID).setTaskStatus(TemplateTask.TaskStatus.DONE);
        } else getTaskById(taskID).setTaskStatus(TemplateTask.TaskStatus.IN_PROGRESS);
    }
}
