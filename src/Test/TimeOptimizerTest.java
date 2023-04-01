package Test;

import Manager.TimeOptimizer;
import Model.SubTask;
import Model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TimeOptimizerTest {


    @Test
    public void shouldOrganizeSubTasksInsideTaskOneByOne() {

        Task t = new Task();

        SubTask s1 = new SubTask();
        SubTask s2 = new SubTask();
        s2.setTaskStartTime(s2.getTaskStartTime().plusSeconds(5));
        SubTask s3 = new SubTask();
        s3.setTaskStartTime(s2.getTaskStartTime());
        SubTask s4 = new SubTask();
        s4.setTaskStartTime(s4.getTaskStartTime().minusSeconds(10));

        assertTrue(s1.getTaskStartTime().isBefore(s2.getTaskStartTime()));
        assertEquals(s2.getTaskStartTime(), s3.getTaskStartTime(), "Время начала двух подзадач одинаковое");
        assertTrue(s4.getTaskStartTime().isBefore(s1.getTaskStartTime()),
                "Время начала подзадач");

        t.getSubTasksList().add(s1);
        t.getSubTasksList().add(s2);
        t.getSubTasksList().add(s3);
        t.getSubTasksList().add(s4);

        assertAll(
                () -> assertEquals(0, t.getSubTasksList().indexOf(s1)),
                () -> assertEquals(1, t.getSubTasksList().indexOf(s2)),
                () -> assertEquals(2, t.getSubTasksList().indexOf(s3)),
                () -> assertEquals(3, t.getSubTasksList().indexOf(s4),
                        "Элементы в списке отражены в порядке записи, то есть без учета времени начала выполнения подзадачи.")
        );
        List<SubTask> organizedSubList = TimeOptimizer.organizeSubTasksTime(t);


        assertAll(

                () -> assertTrue
                        (organizedSubList.get(0).getTaskStartTime()
                                .isBefore(organizedSubList.get(1).getTaskStartTime())),
                () -> assertTrue
                        (organizedSubList.get(1).getTaskStartTime()
                                .isBefore(organizedSubList.get(2).getTaskStartTime())),
                () -> assertTrue
                        (organizedSubList.get(2).getTaskStartTime()
                                        .isBefore(organizedSubList.get(3).getTaskStartTime()),
                                "Элементы отсортированы по дате начала."),

                () -> assertTrue
                        (organizedSubList.get(0).getTaskEndTime()
                                .isBefore(organizedSubList.get(1).getTaskStartTime())),
                () -> assertTrue
                        (organizedSubList.get(1).getTaskEndTime()
                                .isBefore(organizedSubList.get(2).getTaskStartTime())),
                () -> assertTrue
                        (organizedSubList.get(2).getTaskEndTime()
                                        .isBefore(organizedSubList.get(3).getTaskStartTime()),
                                "Подзадачи начинаются после завершения предыдущих."),

                () -> assertEquals(t.getTaskStartTime(), organizedSubList.get(0).getTaskStartTime(),
                        "Время начала задачи и первой подзадачи совпадает."),

                () -> assertNotEquals(s2.getTaskStartTime(), s3.getTaskStartTime(),
                        "Время начала двух подзадач уже не одинаковое.")
        );
    }

    @Test
    public void shouldOrganizeTasksOneByOne() {
        HashMap<Integer, Task> allTasksList = new HashMap<>();
        allTasksList.put(1, new Task());
        allTasksList.put(2, new Task());
        allTasksList.put(3, new Task());

        allTasksList.get(1).setTaskStartTime(Instant.now());
        allTasksList.get(2).setTaskStartTime(allTasksList.get(1).getTaskStartTime());
        allTasksList.get(3).setTaskStartTime(allTasksList.get(1).getTaskStartTime());

        allTasksList.get(1).setTaskDuration(Duration.ofSeconds(10));
        allTasksList.get(2).setTaskDuration(Duration.ofSeconds(10));
        allTasksList.get(3).setTaskDuration(Duration.ofSeconds(10));

        assertEquals
                (allTasksList.get(1).getTaskEndTime(), allTasksList.get(2).getTaskEndTime());
        assertEquals
                (allTasksList.get(2).getTaskEndTime(), allTasksList.get(3).getTaskEndTime(),
                        "Время начала и окончания всех задач до обработки равны.");

        Map<Integer, Task> optimizedTaskTimeList = TimeOptimizer.organizeAllTasksTime(allTasksList);
        assertEquals(3, optimizedTaskTimeList.size());

        for (Task t : optimizedTaskTimeList.values()) {
            System.out.println("Начало: " + t.getTaskStartTime());
            System.out.println("Конец: " + t.getTaskEndTime());
            System.out.println();
        }
        ArrayList<Task> timeTestList = new ArrayList<>(optimizedTaskTimeList.values());

        assertEquals(3, timeTestList.size());
        assertTrue
                (timeTestList.get(0).getTaskEndTime().isBefore(timeTestList.get(1).getTaskStartTime()));
        assertTrue
                (timeTestList.get(1).getTaskEndTime().isBefore(timeTestList.get(2).getTaskStartTime()),
                        "Теперь задачи начинаются поле завершения предыдущих.");
    }
}
