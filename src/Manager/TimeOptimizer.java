
package Manager;

import Model.SubTask;
import Model.Task;
import Model.TemplateTask;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class TimeOptimizer {

    private static final long PERIOD_BETWEEN_SUBTASKS_IN_SECONDS = 300;
    private static final long PERIOD_BETWEEN_TASKS_IN_SECONDS = 600;

    /**
     * method for setting consequent time for subtasks
     * depending on EpicTask start time and subTask' time of creation
     */

    public static List<SubTask> organizeSubTasksTime(Task task) {
        List<SubTask> optimizedSubTasksList;

        if (!task.getSubTasksList().isEmpty()) {
       optimizedSubTasksList = task.getSubTasksList();

            Instant startTime = task.getTaskStartTime();

         optimizedSubTasksList.sort(Comparator.comparing(TemplateTask::getTaskStartTime));

            for (int i = 0; i < optimizedSubTasksList.size(); i++) {
                optimizedSubTasksList.get(i).
                        setTaskStartTime(startTime);
                optimizedSubTasksList.get(i).
                        setTaskEndTime(startTime.plus(optimizedSubTasksList.get(i).getTaskDuration()));

                startTime = optimizedSubTasksList.get(i).
                        getTaskEndTime().plusSeconds(PERIOD_BETWEEN_SUBTASKS_IN_SECONDS);
            }

            return optimizedSubTasksList;
        }
        return null;
    }

    /**
     * method for consequent scheduling all tasks & subtasks one by one
     */

    public static Map<Integer, Task> organizeAllTasksTime(HashMap<Integer, Task> allTasksList) {
        Map<Integer, Task> optimizedTasksList = new HashMap<>();

        List<Task> temporaryTaskList = new ArrayList<>(allTasksList.values());
        temporaryTaskList.sort(Comparator.comparing(Task::getTaskStartTime));

        Instant startTime = temporaryTaskList.get(0).getTaskStartTime();


        for (Task t : temporaryTaskList) {
            t.setTaskStartTime(startTime);

            if (t.isEpic() && !t.getSubTasksList().isEmpty()) {
                t.setSubTasksList(organizeSubTasksTime(t));
                int lastSubTasksIndex = t.getSubTasksList().size() - 1;
                Instant endtime = t.getSubTasksList().get(lastSubTasksIndex).getTaskEndTime();
                t.setTaskEndTime(endtime);


                t.setTaskDuration
                        (Duration.between(t.getTaskEndTime(), t.getTaskStartTime()));

            } else if (!t.isEpic()) {
                t.setTaskEndTime
                        (t.getTaskStartTime().plus(t.getTaskDuration()));
            }

            startTime = t.getTaskEndTime().plusSeconds(PERIOD_BETWEEN_TASKS_IN_SECONDS);

            optimizedTasksList.put(t.getTaskId(), t);
        }

        return optimizedTasksList;
    }

    /**
     * method for creating consequent list all tasks & subtasks one by one due to schedule
     */
    public static List<Task> sortScheduleTasksList(HashMap<Integer, Task> allTasksList) {
        Map<Integer, Task> optimizedTasksList = organizeAllTasksTime(allTasksList);
        List<Task> oneByOneTaskList = new ArrayList<>(optimizedTasksList.values());
        oneByOneTaskList.sort(Comparator.comparing(Task::getTaskStartTime));

        return oneByOneTaskList;

    }

    /**
     * method for consequent output all tasks & subtasks one by one
     */

    public static void printTasksSchedule(HashMap<Integer, Task> allTasksList) {
        List<Task> oneByOneTaskList = sortScheduleTasksList(allTasksList);

        for (Task t : oneByOneTaskList) {
            System.out.println("ID задачи " + t.getTaskId() +
                    " время начала " + t.getTaskStartTime() +
                    " время окончания " + t.getTaskEndTime()
            );

            for (int i = 0; i < t.getSubTasksList().size(); i++) {
                System.out.println("ID подзадачи " + t.getSubTasksList().get(i).getTaskId()
                        + " время начала подзадачи " + t.getSubTasksList().get(i).getTaskStartTime() +
                        " время окончания подзадачи " + t.getSubTasksList().get(i).getTaskEndTime()
                );
            }
        }

    }

}

