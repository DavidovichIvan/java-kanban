/*
Комментарии к тестовому блоку:

Методов много, вижу, что очень обширно и разнообразно к этому вопросу можно подойти, общий смысл вроде понятен.
Такую работу конечно лучше сразу делать по мере написания кода.
Ну и видно, что общий подход к написанию тестов отличается от подхода к основному коду.
Некоторые методы, которые делают одно и тоже, но с разными входными параметрами я объединял в общий тест.
Отдельные методы не тестировал (геттеры/сеттеры и методы, состоящие из протестированных методов).

В ТЗ сказано: "Для каждого метода нужно проверить его работу:
  a. Со стандартным поведением.
  b. С пустым списком задач.
  c. С неверным идентификатором задачи (пустой и/или несуществующий идентификатор)."

Именно что для каждого метода это не совсем корректная постановка задачи, но в целом где уместно эти моменты вошли в тесты.
В целом методы все разные, поэтому ТЗ по тестированию не везде можно реализовать буквально.
 */


package Test;

import Manager.InMemoryTaskManager;
import Manager.Managers;
import Model.SubTask;
import Model.Task;
import Model.TemplateTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static Manager.Feedback.*;


public class TaskManagerTest {

    Task testTask;
    SubTask testSubTask;
    InMemoryTaskManager taskManagerTest = (InMemoryTaskManager) Managers.getDefault();

    final int NON_EXISTING_TEST_ID = -999;


    @BeforeEach
    public void beforeEach() {
        testTask = new Task();
        testSubTask = new SubTask();
    }

    @Test
    public void taskCreationTests() {
        Task t = taskManagerTest.createNewTask();
        Task t1 = taskManagerTest.createNewTask("Тесты", "Много тестов");

        Assertions.assertNotNull(t);
        Assertions.assertTrue(t instanceof Task);

        Assertions.assertNotNull(t);
        Assertions.assertTrue(t1 instanceof Task);

    }

    @Test
    public void addingTaskIntoTasksListTest() {
        Assertions.assertTrue(taskManagerTest.getAllTasksList().isEmpty());
        taskManagerTest.createNewTask(new Task());
        Assertions.assertFalse(taskManagerTest.getAllTasksList().isEmpty());
    }


    @Test
    public void creatingSubtaskForCertainExistingTaskTest() {
        Assertions.assertTrue(testTask.getSubTasksList().isEmpty());

        taskManagerTest.createNewTask(testTask);
        taskManagerTest.createSubTask(testTask.getTaskId());
        taskManagerTest.createSubTask(testTask.getTaskId(), "Имя сабтаска", TemplateTask.TaskStatus.NEW);
        taskManagerTest.createSubTask(testTask.getTaskId(), testSubTask);

        final int expectedSubtasksQuantity = 3;
        String expectedFeedback = String.valueOf(NON_EXISTING_TASK_ID);

        Assertions.assertFalse(testTask.getSubTasksList().isEmpty());
        Assertions.assertEquals(expectedSubtasksQuantity, testTask.getSubTasksList().size());

        Assertions.assertNull(taskManagerTest.createSubTask(NON_EXISTING_TEST_ID));
        Assertions.assertNull(taskManagerTest.createSubTask
                (NON_EXISTING_TEST_ID, "Имя сабтаска", TemplateTask.TaskStatus.NEW));
        Assertions.assertEquals(expectedFeedback, String.valueOf
                (taskManagerTest.createSubTask(NON_EXISTING_TEST_ID, testSubTask)));
    }

    @Test
    public void getTaskByIdTest() {
        final int id = testTask.getTaskId();
        taskManagerTest.createNewTask(testTask);

        Task newTestTask = taskManagerTest.getTaskById(id);
        Assertions.assertEquals(id, newTestTask.getTaskId());
        Assertions.assertNull(taskManagerTest.getTaskById(NON_EXISTING_TEST_ID));

    }

    @Test
    public void getSubTaskByIdTest() {
        final int id = testSubTask.getSubTaskId();
        taskManagerTest.createNewTask(testTask);
        taskManagerTest.createSubTask(testTask.getTaskId(), testSubTask);

        SubTask newTestSubTask = taskManagerTest.getSubTaskById(id);
        Assertions.assertEquals(id, newTestSubTask.getTaskId());
        Assertions.assertNull(taskManagerTest.getSubTaskById(NON_EXISTING_TEST_ID));

    }

    @Test
    public void getAllExistingSubtasksTest() {

        taskManagerTest.createNewTask(testTask);
        taskManagerTest.createSubTask(testTask.getTaskId(), testSubTask);

        List<SubTask> testSubTasksList = taskManagerTest.getAllExistingSubtasks();

        Assertions.assertNotNull(testSubTasksList);
        Assertions.assertTrue(testSubTasksList.contains(testSubTask));
    }

    @Test
    public void getSubtasksForCertainTaskByIdTest() {

        taskManagerTest.createNewTask(testTask);
        taskManagerTest.createSubTask(testTask.getTaskId(), testSubTask);

        List<SubTask> testSubTasksList = taskManagerTest.getSubtasksForCertainTaskByID(testTask.getTaskId());

        Assertions.assertNotNull(testSubTasksList);
        Assertions.assertTrue(testSubTasksList.contains(testSubTask));
        Assertions.assertNull(taskManagerTest.getSubtasksForCertainTaskByID(NON_EXISTING_TEST_ID));

    }

    @Test
    public void getOnlyEpicTasksTest() {

        final int id = testTask.getTaskId();
        taskManagerTest.createNewTask(testTask);

        taskManagerTest.getAllTasksList().get(id).setEpic(false);
        Assertions.assertTrue(taskManagerTest.getAllEpicTasksList().isEmpty());

        final int expectedSubtasksQuantity = 1;
        taskManagerTest.getAllTasksList().get(id).setEpic(true);

        Assertions.assertTrue(!taskManagerTest.getAllEpicTasksList().isEmpty());
        Assertions.assertEquals(expectedSubtasksQuantity, taskManagerTest.getAllEpicTasksList().size());

    }

    @Test
    public void getOnlyNonEpicTasksTest() {

        Task t1 = new Task();
        t1.setEpic(true);
        taskManagerTest.createNewTask(t1);
        Task t2 = new Task();
        t2.setEpic(true);
        taskManagerTest.createNewTask(t2);
        Task t3 = new Task();
        t3.setEpic(false);
        taskManagerTest.createNewTask(t3);
        Task t4 = new Task();
        t4.setEpic(false);
        taskManagerTest.createNewTask(t4);

        final int expectedListSize = 2;
        Assertions.assertEquals(expectedListSize, taskManagerTest.getAllNonEpicTasksList().size());

        Assertions.assertFalse(taskManagerTest.getAllNonEpicTasksList().containsValue(t1));
        Assertions.assertFalse(taskManagerTest.getAllNonEpicTasksList().containsValue(t2));
        Assertions.assertTrue(taskManagerTest.getAllNonEpicTasksList().containsValue(t3));
        Assertions.assertTrue(taskManagerTest.getAllNonEpicTasksList().containsValue(t4));

    }

    @Test
    public void deleteAllTasksTest() {
        taskManagerTest.createNewTask(testTask);
        Assertions.assertFalse(taskManagerTest.getAllTasksList().isEmpty());

        taskManagerTest.deleteAllTasks();
        Assertions.assertTrue(taskManagerTest.getAllTasksList().isEmpty());
        Assertions.assertTrue(taskManagerTest.getHistory().getHistoryList().isEmpty());
    }

    @Test
    public void deleteTaskByIdTest() {

        Task t1 = new Task();
        taskManagerTest.createNewTask(t1);
        Task t2 = new Task();
        taskManagerTest.createNewTask(t2);
        Assertions.assertTrue(taskManagerTest.getAllTasksList().containsValue(t1));
        Assertions.assertTrue(taskManagerTest.getAllTasksList().containsValue(t2));

        taskManagerTest.deleteTaskById(NON_EXISTING_TEST_ID);
        taskManagerTest.deleteTaskById(t1.getTaskId());
        Assertions.assertFalse(taskManagerTest.getAllTasksList().containsValue(t1));
        Assertions.assertTrue(taskManagerTest.getAllTasksList().containsValue(t2));

    }

    @Test
    public void deleteOneSubTaskByIdTest() {

        SubTask s1 = new SubTask();
        SubTask s2 = new SubTask();

        taskManagerTest.createNewTask(testTask);

        taskManagerTest.createSubTask(testTask.getTaskId(), s1);
        taskManagerTest.createSubTask(testTask.getTaskId(), s2);
        Assertions.assertTrue(taskManagerTest.getTaskById(testTask.getTaskId()).getSubTasksList().contains(s1));
        Assertions.assertTrue(taskManagerTest.getTaskById(testTask.getTaskId()).getSubTasksList().contains(s2));

        taskManagerTest.deleteSubTaskByID(s2.getSubTaskId());

        Assertions.assertTrue(taskManagerTest.getTaskById(testTask.getTaskId()).getSubTasksList().contains(s1));
        Assertions.assertTrue(!taskManagerTest.getTaskById(testTask.getTaskId()).getSubTasksList().contains(s2));

        String expectedFeedback = String.valueOf(FAILED_TO_DELETE_SUBTASK_NON_EXISTING_ID);
        Assertions.assertEquals(expectedFeedback, String.valueOf
                (taskManagerTest.deleteSubTaskByID(NON_EXISTING_TEST_ID)));

    }

    @Test
    public void deleteAllSubTasksByTaskIdTest() {
        taskManagerTest.createNewTask(testTask);
        SubTask s1 = new SubTask();
        SubTask s2 = new SubTask();

        taskManagerTest.createSubTask(testTask.getTaskId(), s1);
        taskManagerTest.createSubTask(testTask.getTaskId(), s2);

        Assertions.assertTrue(testTask.getSubTasksList().contains(s1));
        Assertions.assertTrue(testTask.getSubTasksList().contains(s2));
        Assertions.assertTrue(!testTask.getSubTasksList().isEmpty());

        String expectedFeedback = String.valueOf(NON_EXISTING_TASK_ID);
        Assertions.assertEquals(expectedFeedback, String.valueOf
                (taskManagerTest.deleteAllSubTasksByTaskId(NON_EXISTING_TEST_ID)));

        taskManagerTest.deleteAllSubTasksByTaskId(testTask.getTaskId());
        Assertions.assertTrue(testTask.getSubTasksList().isEmpty());

    }

    @Test
    public void updateSubTasksByIdTest() {
        taskManagerTest.createNewTask(testTask);
        SubTask oldSub = new SubTask("Старая подзадача", TemplateTask.TaskStatus.NEW);
        final int id = oldSub.getTaskId();

        taskManagerTest.createSubTask(testTask.getTaskId(), oldSub);

        int expectedSubtasksListSize = 1;
        Assertions.assertEquals(testTask.getSubTasksList().size(), expectedSubtasksListSize);

        SubTask newSub = new SubTask("Новая подзадача", TemplateTask.TaskStatus.NEW);
        taskManagerTest.updateSubTask(newSub, id);

        Assertions.assertEquals(testTask.getSubTasksList().size(), expectedSubtasksListSize);
        Assertions.assertTrue(testTask.getSubTasksList().contains(newSub));

        String expectedFeedback = String.valueOf(FAILED_TO_UPDATE_SUBTASK_NON_EXISTING_ID);
        Assertions.assertEquals(expectedFeedback, String.valueOf
                (taskManagerTest.updateSubTask(new SubTask(), NON_EXISTING_TEST_ID)));
    }

    @Test
    public void methodsForChangeTaskNameAndDescriptionsByItsIdTest() {

        final int id = taskManagerTest.createNewTask("Старое имя", "Старое описание").getTaskId();

        taskManagerTest.changeTaskNameById(id, "Новое имя");
        taskManagerTest.changeTaskDescriptionById(id, "Новое описание");

        String expectedName = "Новое имя";
        String expectedDescription = "Новое описание";

        Assertions.assertEquals(expectedName, taskManagerTest.getTaskById(id).getName());
        Assertions.assertEquals(expectedDescription, taskManagerTest.getTaskById(id).getTaskDescription());

        String expectedResult = String.valueOf(NON_EXISTING_TASK_ID);
        Assertions.assertEquals(expectedResult, String.valueOf
                (taskManagerTest.changeTaskNameById(NON_EXISTING_TEST_ID, "Новое имя")));
        Assertions.assertEquals(expectedResult, String.valueOf
                (taskManagerTest.changeTaskDescriptionById(NON_EXISTING_TEST_ID, "Новое имя")));

    }

    @Test
    public void changeNonEpicTaskStatusByIdTest() {
        Assertions.assertFalse(testTask.isEpic());
        taskManagerTest.createNewTask(testTask);

        String expectedResult = String.valueOf(NON_EXISTING_TASK_ID);
        Assertions.assertEquals(expectedResult, String.valueOf
                (taskManagerTest.changeNonEpicTaskStatusById(NON_EXISTING_TEST_ID, TemplateTask.TaskStatus.DONE)));

        Assertions.assertSame(testTask.getTaskStatus(), TemplateTask.TaskStatus.NEW);
        taskManagerTest.changeNonEpicTaskStatusById(testTask.getTaskId(), TemplateTask.TaskStatus.DONE);
        Assertions.assertSame(testTask.getTaskStatus(), TemplateTask.TaskStatus.DONE);


        String expectedResult1 = String.valueOf(UNABLE_TO_UPDATE_STATUS_FOR_EPIC_TASK);
        Task testTask1 = new Task();
        testTask1.setEpic(true);
        taskManagerTest.createNewTask(testTask1);
        Assertions.assertTrue(testTask1.isEpic());
        Assertions.assertEquals
                (expectedResult1, String.valueOf(taskManagerTest.changeNonEpicTaskStatusById(testTask1.getTaskId(),
                        TemplateTask.TaskStatus.IN_PROGRESS)));

    }

    @Test
    public void shouldChangeTaskStartTime() {

        Instant newTime = Instant.now().plusSeconds(5);
        Assertions.assertFalse(testTask.getTaskStartTime().equals(newTime), "Время не равно newTime");

        taskManagerTest.getAllTasksList().put(testTask.getTaskId(), testTask);
        taskManagerTest.setTaskStartTime(testTask.getTaskId(), newTime);
        Assertions.assertTrue(testTask.getTaskStartTime().equals(newTime), "Время изменено на newTime.");
    }

    @Test
    public void setTaskEndTimeByLastSubtaskTimeAndRecalculateTaskDurationTest() {

        int taskID = testTask.getTaskId();
        testTask.setTaskStartTime(Instant.now());

        taskManagerTest.createNewTask(testTask);
        taskManagerTest.setNonEpicTaskDuration(taskID, 10);

        SubTask s1 = new SubTask();
        s1.setTaskDuration(Duration.ofMinutes(5));
        taskManagerTest.createSubTask(taskID, s1);

        SubTask s2 = new SubTask();
        s2.setTaskDuration(Duration.ofMinutes(11));
        taskManagerTest.createSubTask(taskID, s2);

        SubTask s3 = new SubTask();
        s3.setTaskDuration(Duration.ofMinutes(15));
        taskManagerTest.createSubTask(taskID, s3);
        Duration expectedTaskDuration = s3.getTaskDuration();

        Assertions.assertNotEquals(taskManagerTest.getTaskById(taskID).getTaskEndTime(), s3.getTaskEndTime());

        taskManagerTest.setTaskEndTimeByLastSubtaskTimeAndRecalculateTaskDuration(taskID);
        Assertions.assertTrue(taskManagerTest.getTaskById(taskID).getTaskEndTime().equals(s3.getTaskEndTime()),
                "Время завершения задачи установлено как время завершения последней подзадачи.");
        Assertions.assertEquals(expectedTaskDuration, taskManagerTest.getTaskById(taskID).getTaskDuration(),
                "Продолжительность изменена, с учетом изменения времени заверения.");

    }
}

