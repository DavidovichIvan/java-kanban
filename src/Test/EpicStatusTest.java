package Test;

import Manager.InMemoryTaskManager;
import Manager.Managers;
import Model.Task;
import Model.TemplateTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EpicStatusTest {
    int id;
    InMemoryTaskManager taskManagerTest = (InMemoryTaskManager) Managers.getDefaultOld();
    TemplateTask.TaskStatus expectedStatus;

    @BeforeEach
    public void beforeEach() {
        expectedStatus = TemplateTask.TaskStatus.NEW;
        id = taskManagerTest.createNewTask("Наводим порядок", "Уборка").getTaskId();
    }

    @Test //Пустой список подзадач.
    public void StatusShouldBeNewForNewTaskWithNoSubtasks() {
        Task t = new Task();
        Task t1 = new Task("Готовим", "Готовим ужин");

        t.setSubTasksList(null);
        t1.setSubTasksList(null);

        assertAll(
                ()-> assertEquals(expectedStatus, t.getTaskStatus()),
                ()-> assertEquals(expectedStatus, t1.getTaskStatus())
        );
    }

    @Test //Все подзадачи со статусом NEW.
    public void StatusShouldBeNewIfAllSubTaskStatusesAreNew() {

        taskManagerTest.createSubTask(id, "Уборка в гостинной", TemplateTask.TaskStatus.NEW);
        int subId = taskManagerTest.createSubTask
                (id, "Уборка в ванной", TemplateTask.TaskStatus.IN_PROGRESS).getSubTaskId();
        expectedStatus = TemplateTask.TaskStatus.IN_PROGRESS;
        assertEquals
                (expectedStatus, taskManagerTest.getTaskById(id).getTaskStatus());

        taskManagerTest.getSubTaskById(subId).setTaskStatus(TemplateTask.TaskStatus.NEW);
        taskManagerTest.updateTaskStatus(id);

        expectedStatus = TemplateTask.TaskStatus.NEW;
        assertEquals
                (expectedStatus, taskManagerTest.getTaskById(id).getTaskStatus());
    }

    @Test //Все подзадачи со статусом DONE.
    public void StatusShouldBeDoneIfAllSubTaskStatusesAreDone() {
        taskManagerTest.createSubTask(id, "Уборка в гостинной", TemplateTask.TaskStatus.DONE);
        taskManagerTest.createSubTask(id, "Уборка в ванной", TemplateTask.TaskStatus.DONE);

        expectedStatus = TemplateTask.TaskStatus.DONE;
        assertEquals
                (expectedStatus, taskManagerTest.getTaskById(id).getTaskStatus());
    }

    @Test //Подзадачи со статусами NEW и DONE.
    public void StatusShouldBeInProgressIfSomeSubTasksDoneAndSomeAreNew() {
        taskManagerTest.createSubTask(id, "Уборка в гостинной", TemplateTask.TaskStatus.DONE);
        taskManagerTest.createSubTask(id, "Уборка в ванной", TemplateTask.TaskStatus.NEW);

        expectedStatus = TemplateTask.TaskStatus.IN_PROGRESS;
        assertEquals
                (expectedStatus, taskManagerTest.getTaskById(id).getTaskStatus());
    }

   @Test //Подзадачи со статусом IN_PROGRESS.
   public void StatusShouldBeInProgressIfAnyOfSubTaskIsInProgress() {
       taskManagerTest.createSubTask(id, "Уборка в гостинной", TemplateTask.TaskStatus.NEW);
       assertEquals
               (expectedStatus, taskManagerTest.getTaskById(id).getTaskStatus());

       taskManagerTest.createSubTask(id, "Уборка в ванной", TemplateTask.TaskStatus.IN_PROGRESS);

       expectedStatus = TemplateTask.TaskStatus.IN_PROGRESS;
       assertEquals
               (expectedStatus, taskManagerTest.getTaskById(id).getTaskStatus());

   }

}