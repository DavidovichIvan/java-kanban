
package Test;

import Manager.InMemoryTaskManager;
import Manager.Managers;
import Model.SubTask;
import Model.Task;
import Model.TemplateTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static Manager.Feedback.*;
import static org.junit.jupiter.api.Assertions.*;


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

        assertNotNull(t);
        assertTrue(t instanceof Task);

        assertNotNull(t);
        assertTrue(t1 instanceof Task);

    }

    @Test
    public void addingTaskIntoTasksListTest() {
        assertTrue(taskManagerTest.getAllTasksList().isEmpty());
        taskManagerTest.createNewTask(new Task());
        assertFalse(taskManagerTest.getAllTasksList().isEmpty());
    }


    @Test
    public void creatingSubtaskForCertainExistingTaskTest() {
        assertTrue(testTask.getSubTasksList().isEmpty());

        taskManagerTest.createNewTask(testTask);
        taskManagerTest.createSubTask(testTask.getTaskId());
        taskManagerTest.createSubTask(testTask.getTaskId(), "Имя сабтаска", TemplateTask.TaskStatus.NEW);
        taskManagerTest.createSubTask(testTask.getTaskId(), testSubTask);

        final int expectedSubtasksQuantity = 3;
        String expectedFeedback = String.valueOf(NON_EXISTING_TASK_ID);

        assertFalse(testTask.getSubTasksList().isEmpty());
        assertEquals(expectedSubtasksQuantity, testTask.getSubTasksList().size());

        assertNull(taskManagerTest.createSubTask(NON_EXISTING_TEST_ID));
        assertNull(taskManagerTest.createSubTask
                (NON_EXISTING_TEST_ID, "Имя сабтаска", TemplateTask.TaskStatus.NEW));
        assertEquals(expectedFeedback, String.valueOf
                (taskManagerTest.createSubTask(NON_EXISTING_TEST_ID, testSubTask)));
    }

    @Test
    public void getTaskByIdTest() {
        final int id = testTask.getTaskId();
        taskManagerTest.createNewTask(testTask);

        Task newTestTask = taskManagerTest.getTaskById(id);
        assertEquals(id, newTestTask.getTaskId());
        assertNull(taskManagerTest.getTaskById(NON_EXISTING_TEST_ID));

    }

    @Test
    public void getSubTaskByIdTest() {
        final int id = testSubTask.getSubTaskId();
        taskManagerTest.createNewTask(testTask);
        taskManagerTest.createSubTask(testTask.getTaskId(), testSubTask);

        SubTask newTestSubTask = taskManagerTest.getSubTaskById(id);
        assertEquals(id, newTestSubTask.getTaskId());
        assertNull(taskManagerTest.getSubTaskById(NON_EXISTING_TEST_ID));

    }

    @Test
    public void getAllExistingSubtasksTest() {

        taskManagerTest.createNewTask(testTask);
        taskManagerTest.createSubTask(testTask.getTaskId(), testSubTask);

        List<SubTask> testSubTasksList = taskManagerTest.getAllExistingSubtasks();

        assertNotNull(testSubTasksList);
        assertTrue(testSubTasksList.contains(testSubTask));
    }

    @Test
    public void getSubtasksForCertainTaskByIdTest() {

        taskManagerTest.createNewTask(testTask);
        taskManagerTest.createSubTask(testTask.getTaskId(), testSubTask);

        List<SubTask> testSubTasksList = taskManagerTest.getSubtasksForCertainTaskByID(testTask.getTaskId());

        assertNotNull(testSubTasksList);
        assertTrue(testSubTasksList.contains(testSubTask));
        assertNull(taskManagerTest.getSubtasksForCertainTaskByID(NON_EXISTING_TEST_ID));

    }

    @Test
    public void getOnlyEpicTasksTest() {

        final int id = testTask.getTaskId();
        taskManagerTest.createNewTask(testTask);

        taskManagerTest.getAllTasksList().get(id).setEpic(false);
        assertTrue(taskManagerTest.getAllEpicTasksList().isEmpty());

        final int expectedSubtasksQuantity = 1;
        taskManagerTest.getAllTasksList().get(id).setEpic(true);

        assertTrue(!taskManagerTest.getAllEpicTasksList().isEmpty());
        assertEquals(expectedSubtasksQuantity, taskManagerTest.getAllEpicTasksList().size());

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
        assertEquals(expectedListSize, taskManagerTest.getAllNonEpicTasksList().size());

        assertFalse(taskManagerTest.getAllNonEpicTasksList().containsValue(t1));
        assertFalse(taskManagerTest.getAllNonEpicTasksList().containsValue(t2));
        assertTrue(taskManagerTest.getAllNonEpicTasksList().containsValue(t3));
        assertTrue(taskManagerTest.getAllNonEpicTasksList().containsValue(t4));

    }

    @Test
    public void deleteAllTasksTest() {
        taskManagerTest.createNewTask(testTask);
        assertFalse(taskManagerTest.getAllTasksList().isEmpty());

        taskManagerTest.deleteAllTasks();
        assertTrue(taskManagerTest.getAllTasksList().isEmpty());
        assertTrue(taskManagerTest.getHistory().getHistoryList().isEmpty());
    }

    @Test
    public void deleteTaskByIdTest() {

        Task t1 = new Task();
        taskManagerTest.createNewTask(t1);
        Task t2 = new Task();
        taskManagerTest.createNewTask(t2);
        assertTrue(taskManagerTest.getAllTasksList().containsValue(t1));
        assertTrue(taskManagerTest.getAllTasksList().containsValue(t2));

        taskManagerTest.deleteTaskById(NON_EXISTING_TEST_ID);
        taskManagerTest.deleteTaskById(t1.getTaskId());
        assertFalse(taskManagerTest.getAllTasksList().containsValue(t1));
        assertTrue(taskManagerTest.getAllTasksList().containsValue(t2));

    }

    @Test
    public void deleteOneSubTaskByIdTest() {

        SubTask s1 = new SubTask();
        SubTask s2 = new SubTask();

        taskManagerTest.createNewTask(testTask);

        taskManagerTest.createSubTask(testTask.getTaskId(), s1);
        taskManagerTest.createSubTask(testTask.getTaskId(), s2);
        assertTrue
                (taskManagerTest.getTaskById(testTask.getTaskId()).getSubTasksList().contains(s1));
        assertTrue
                (taskManagerTest.getTaskById(testTask.getTaskId()).getSubTasksList().contains(s2));

        taskManagerTest.deleteSubTaskByID(s2.getSubTaskId());

        assertTrue
                (taskManagerTest.getTaskById(testTask.getTaskId()).getSubTasksList().contains(s1));
        assertTrue
                (!taskManagerTest.getTaskById(testTask.getTaskId()).getSubTasksList().contains(s2));

        String expectedFeedback = String.valueOf(FAILED_TO_DELETE_SUBTASK_NON_EXISTING_ID);
        assertEquals(expectedFeedback, String.valueOf
                (taskManagerTest.deleteSubTaskByID(NON_EXISTING_TEST_ID)));

    }

    @Test
    public void deleteAllSubTasksByTaskIdTest() {
        taskManagerTest.createNewTask(testTask);
        SubTask s1 = new SubTask();
        SubTask s2 = new SubTask();

        taskManagerTest.createSubTask(testTask.getTaskId(), s1);
        taskManagerTest.createSubTask(testTask.getTaskId(), s2);

        assertTrue(testTask.getSubTasksList().contains(s1));
        assertTrue(testTask.getSubTasksList().contains(s2));
        assertTrue(!testTask.getSubTasksList().isEmpty());

        String expectedFeedback = String.valueOf(NON_EXISTING_TASK_ID);
        assertEquals(expectedFeedback, String.valueOf
                (taskManagerTest.deleteAllSubTasksByTaskId(NON_EXISTING_TEST_ID)));

        taskManagerTest.deleteAllSubTasksByTaskId(testTask.getTaskId());
        assertTrue(testTask.getSubTasksList().isEmpty());

    }

    @Test
    public void updateSubTasksByIdTest() {
        taskManagerTest.createNewTask(testTask);
        SubTask oldSub = new SubTask("Старая подзадача", TemplateTask.TaskStatus.NEW);
        final int id = oldSub.getTaskId();

        taskManagerTest.createSubTask(testTask.getTaskId(), oldSub);

        int expectedSubtasksListSize = 1;
        assertEquals(testTask.getSubTasksList().size(), expectedSubtasksListSize);

        SubTask newSub = new SubTask("Новая подзадача", TemplateTask.TaskStatus.NEW);
        taskManagerTest.updateSubTask(newSub, id);

        assertEquals(testTask.getSubTasksList().size(), expectedSubtasksListSize);
        assertTrue(testTask.getSubTasksList().contains(newSub));

        String expectedFeedback = String.valueOf(FAILED_TO_UPDATE_SUBTASK_NON_EXISTING_ID);
        assertEquals
                (expectedFeedback, String.valueOf
                (taskManagerTest.updateSubTask(new SubTask(), NON_EXISTING_TEST_ID)));
    }

    @Test
    public void methodsForChangeTaskNameAndDescriptionsByItsIdTest() {

        final int id = taskManagerTest.createNewTask
                ("Старое имя", "Старое описание").getTaskId();

        taskManagerTest.changeTaskNameById(id, "Новое имя");
        taskManagerTest.changeTaskDescriptionById(id, "Новое описание");

        String expectedName = "Новое имя";
        String expectedDescription = "Новое описание";

        assertEquals
                (expectedName, taskManagerTest.getTaskById(id).getName());
        assertEquals
                (expectedDescription, taskManagerTest.getTaskById(id).getTaskDescription());

        String expectedResult = String.valueOf(NON_EXISTING_TASK_ID);
        assertEquals
                (expectedResult, String.valueOf
                (taskManagerTest.changeTaskNameById(NON_EXISTING_TEST_ID,
                        "Новое имя")));
        assertEquals
                (expectedResult, String.valueOf
                (taskManagerTest.changeTaskDescriptionById(NON_EXISTING_TEST_ID,
                        "Новое имя")));

    }

    @Test
    public void changeNonEpicTaskStatusByIdTest() {
        assertFalse(testTask.isEpic());
        taskManagerTest.createNewTask(testTask);

        String expectedResult = String.valueOf(NON_EXISTING_TASK_ID);
        assertEquals
                (expectedResult, String.valueOf
                (taskManagerTest.changeNonEpicTaskStatusById(NON_EXISTING_TEST_ID,
                        TemplateTask.TaskStatus.DONE)));

        assertSame
                (testTask.getTaskStatus(), TemplateTask.TaskStatus.NEW);

        taskManagerTest.changeNonEpicTaskStatusById(testTask.getTaskId(), TemplateTask.TaskStatus.DONE);

       assertSame
                (testTask.getTaskStatus(), TemplateTask.TaskStatus.DONE);


        String expectedResult1 = String.valueOf(UNABLE_TO_UPDATE_STATUS_FOR_EPIC_TASK);
        Task testTask1 = new Task();
        testTask1.setEpic(true);
        taskManagerTest.createNewTask(testTask1);
        assertTrue(testTask1.isEpic());
        assertEquals
                (expectedResult1, String.valueOf
                        (taskManagerTest.changeNonEpicTaskStatusById(testTask1.getTaskId(),
                        TemplateTask.TaskStatus.IN_PROGRESS)));

    }

    @Test
    public void shouldChangeTaskStartTime() {

        Instant newTime = Instant.now().plusSeconds(5);
        assertFalse
                (testTask.getTaskStartTime().equals(newTime),
                        "Время не равно newTime");

        taskManagerTest.getAllTasksList().put(testTask.getTaskId(), testTask);
        taskManagerTest.setTaskStartTime(testTask.getTaskId(), newTime);
        assertTrue
                (testTask.getTaskStartTime().equals(newTime),
                        "Время изменено на newTime.");
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

        assertNotEquals
                (taskManagerTest.getTaskById(taskID).getTaskEndTime(), s3.getTaskEndTime());

        taskManagerTest.setTaskEndTimeByLastSubtaskTimeAndRecalculateTaskDuration(taskID);
        assertTrue
                (taskManagerTest.getTaskById(taskID).getTaskEndTime().equals(s3.getTaskEndTime()),
                "Время завершения задачи установлено как время завершения последней подзадачи.");


    }
}

