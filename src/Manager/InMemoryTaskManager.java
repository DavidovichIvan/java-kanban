/* Комментарии по доработке:
По форматированию длинных строк, содержащих несколько действий - прошел по тексту,
разделил наиболее длинные, запомню, мне тож не нравилось, теперь буду сразу разделять.

По коллекциям прошелся в TimeOptimizer заменил на интерфейсы.

По замене коллекции хранения подзадач со списка на TreeSet чтобы сразу элементы сортировались по времени,
спасибо за подсказку, механизм понятен. Согласен что лучше сразу сохранять как нужно.
Как я посмотрел в данном случае такая замена тянет много моментов за собой по всей программе.
Попробую в дальнейшем переделать аккуратно.

Названия методов в TimeOptimizer заменил на глаголы.

Очень полезная подсказка про assertAll(), по смыслу полезно и визуально тоже более структурированный вид у кода.
Реализовал в нескольких местах. Взял на вооружение.

Видна польза от уже написанных тестов при их повтороном использованиии после каких-либо изменений в коде.

 */


package Manager;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import Model.SubTask;
import Model.Task;
import Model.TemplateTask;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    public void setAllTasksList(Map<Integer, Task> allTasksList) {
        this.allTasksList = allTasksList;
    }

    protected Map<Integer, Task> allTasksList = new HashMap<>();

    protected HistoryManager history;

    public InMemoryTaskManager(HistoryManager history) {
        this.history = history;
    }

    public HistoryManager getHistory() {
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
            getAllTasksList().
                    get(mainTaskId).
                    getSubTasksList().
                    add(subTask);
            updateTaskStatus(mainTaskId);
            if (!getAllTasksList().get(mainTaskId).getSubTasksList().isEmpty()) {
                getAllTasksList().get(mainTaskId).setEpic(true);
            }
            return Feedback.SUBTASK_SUCCESSFULLY_ADDED;
        }
    }

    @Override
    public Task getTaskById(int id) {

        if (!allTasksList.containsKey(id)) {
            return null;
        } else {
            history.updateHistoryList(allTasksList.get(id));
            return allTasksList.get(id);
        }
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
        if (!allTasksList.containsKey(id)) {
            return null;
        } else {
            return getTaskById(id).getSubTasksList();
        }
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
    public Feedback deleteAllSubTasksByTaskId(int id) {

        if (!allTasksList.containsKey(id)) {
            return Feedback.NON_EXISTING_TASK_ID;
        } else {
            for (SubTask sub : getAllTasksList().get(id).getSubTasksList()) {
                history.removeFromHistory(sub.getSubTaskId());
            }

            getAllTasksList().get(id).getSubTasksList().clear();
            getAllTasksList().get(id).setEpic(false);
            return Feedback.SUBTASKS_SUCCESSFULLY_DELETED;
        }
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
            allTasksList.
                    get(taskID).
                    setTaskDescription(newTaskDescription);
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
            getTaskById(taskID).
                    setTaskStatus(TemplateTask.TaskStatus.DONE);
        } else getTaskById(taskID).
                setTaskStatus(TemplateTask.TaskStatus.IN_PROGRESS);
    }

    @Override
    public Feedback setTaskStartTime(int taskID, Instant startTime) {

        if (!allTasksList.containsKey(taskID)) {
            return Feedback.NON_EXISTING_TASK_ID;
        }

        if (!getTaskById(taskID).isEpic() || (!getTaskById(taskID).isEpic()
                && getTaskById(taskID).getSubTasksList().isEmpty())) {
            getTaskById(taskID).setTaskStartTime(startTime);
            getTaskById(taskID).
                    setTaskEndTime(getTaskById(taskID).
                            calculateTaskEndTime());
        } else if (getTaskById(taskID).isEpic()
                && !getTaskById(taskID).getSubTasksList().isEmpty()) {
            getTaskById(taskID).setTaskStartTime(startTime);

            getTaskById(taskID).setSubTasksList(TimeOptimizer.
                    organizeSubTasksTime(getTaskById(taskID)));
            setTaskEndTimeByLastSubtaskTimeAndRecalculateTaskDuration(taskID);

        }

        return Feedback.START_TIME_SUCCESSFULLY_UPDATED;
    }

    @Override
    public Feedback setSubTaskStartTime(int taskID, int subId, Instant startTime) {
        if (!allTasksList.containsKey(taskID)) {
            return Feedback.NON_EXISTING_TASK_ID;
        }

        int subTaskIndexInSubTasksList = 0;
        boolean isSubInsideTask = false;

        for (SubTask s : allTasksList.get(taskID).getSubTasksList()) {

            if (s.getSubTaskId() == subId) {
                isSubInsideTask = true;
                break;
            }
            subTaskIndexInSubTasksList++;
        }

        if (!isSubInsideTask) {
            return Feedback.NO_SUCH_SUBTASK_ID_FOR_TASK_GIVEN;
        }

        if (startTime.isBefore(allTasksList.get(taskID).getTaskStartTime())) {
            allTasksList.get(taskID).setTaskStartTime(startTime);
        }

        allTasksList.get(taskID).
                getSubTasksList().
                get(subTaskIndexInSubTasksList).
                setTaskStartTime(startTime);

        allTasksList.get(taskID).
                setSubTasksList(TimeOptimizer.
                        organizeSubTasksTime(allTasksList.get(taskID)));

        setTaskEndTimeByLastSubtaskTimeAndRecalculateTaskDuration(taskID);

        return Feedback.START_TIME_SUCCESSFULLY_UPDATED;
    }

    /**
     * Auxiliary method for Task end time correction depending on last Subtask ending time
     *
     * @param taskID
     */
    public void setTaskEndTimeByLastSubtaskTimeAndRecalculateTaskDuration(int taskID) {
        int lastElement = getTaskById(taskID).getSubTasksList().size() - 1;

       //temporaryTaskList.sort(Comparator.comparing(Task::getTaskStartTime));
      //  getTaskById(taskID).setSubTasksList(TimeOptimizer.subTasksTimeOrganizer(allTasksList.get(allTasksList)));

        Instant lastSubEndTime = getTaskById(taskID).
                getSubTasksList().
                get(lastElement).
                getTaskEndTime();
        getTaskById(taskID).setTaskEndTime(lastSubEndTime);

        Instant start = getTaskById(taskID).getTaskStartTime();
        Instant finish = getTaskById(taskID).getTaskEndTime();

        getTaskById(taskID).setTaskDuration(Duration.between(start, finish));


    }

    @Override
    public Feedback setNonEpicTaskDuration(int taskID, long durationInMinutes) {
        if (!allTasksList.containsKey(taskID)) {
            return Feedback.NON_EXISTING_TASK_ID;
        }

        if (allTasksList.get(taskID).isEpic()) {
            return Feedback.UNABLE_TO_CHANGE_DURATION_FOR_EPIC_TASK;
        }

        allTasksList.get(taskID).
                setTaskDuration(Duration.ofMinutes(durationInMinutes));
        allTasksList.get(taskID).
                setTaskEndTime(allTasksList.get(taskID).calculateTaskEndTime());

        return Feedback.DURATION_UPDATED;
    }

    @Override
    public Feedback setSubTaskDuration(int taskID, int subId, long durationInMinutes) {
        if (!allTasksList.containsKey(taskID)) {
            return Feedback.NON_EXISTING_TASK_ID;
        }

        int subTaskIndexInSubTasksList = 0;
        boolean isSubInsideTask = false;

        for (SubTask s : allTasksList.get(taskID).getSubTasksList()) {

            if (s.getSubTaskId() == subId) {
                isSubInsideTask = true;
                break;
            }
            subTaskIndexInSubTasksList++;
        }

        if (isSubInsideTask == false) {
            return Feedback.NO_SUCH_SUBTASK_ID_FOR_TASK_GIVEN;
        }

        allTasksList.get(taskID).getSubTasksList()
                .get(subTaskIndexInSubTasksList).
                setTaskDuration(Duration.ofMinutes(durationInMinutes));
        allTasksList.get(taskID).
                setSubTasksList(TimeOptimizer.
                        organizeSubTasksTime(allTasksList.get(taskID)));
        setTaskEndTimeByLastSubtaskTimeAndRecalculateTaskDuration(taskID);

        return Feedback.DURATION_UPDATED;

    }

    @Override
    public Feedback organizeSubTasksScheduleForSingleTask(int taskID) {
        if (!allTasksList.containsKey(taskID)) {
            return Feedback.NON_EXISTING_TASK_ID;
        }

        if (getTaskById(taskID).isEpic() &&
                !getTaskById(taskID).getSubTasksList().isEmpty()) {
            getTaskById(taskID).
                    setSubTasksList(TimeOptimizer.organizeSubTasksTime(getTaskById(taskID)));
            return Feedback.SUBTASKS_SCHEDULE_UPDATED;
        } else return Feedback.NO_SUBTASKS_TO_BE_RESCHEDULED_FOUND;
    }

    @Override
    public Feedback organizeScheduleForAllTasks() {
        if (allTasksList.isEmpty()) {
            return Feedback.NO_TASKS_TO_BE_RESCHEDULED_FOUND;
        }
        setAllTasksList(TimeOptimizer.organizeAllTasksTime((HashMap<Integer, Task>) allTasksList));
        return Feedback.SUBTASKS_SCHEDULE_UPDATED;

    }

    @Override
    public List<Task> getPrioritizedTasks(HashMap<Integer, Task> allTasksList) {
        return TimeOptimizer.sortScheduleTasksList(allTasksList);

    }


}