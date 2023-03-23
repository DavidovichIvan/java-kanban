/* Комментарии:
В этом классе реализованы тесты по 1му (из 4) блоку ТЗ.

Во 2ом блоке ТЗ есть такое задание:
"Для подзадач нужно дополнительно проверить наличие эпика, а для эпика — расчёт статуса."

Расчет статуса для эпика собственно тестируется в этом классе.

Наличие эпика для подзадач проверять в моей реализаци не нужно, так как
подзадачи сохраняются внутри поля-списка основной задачи,
то есть в логике программы не существуют сами по себе в отдельном от эпика хранилище.
*/


package Test;

import Manager.InMemoryTaskManager;
import Manager.Managers;
import Model.Task;
import Model.TemplateTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicStatusTest {
    int id;
    InMemoryTaskManager taskManagerTest = (InMemoryTaskManager) Managers.getDefault();
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

        Assertions.assertEquals(expectedStatus, t.getTaskStatus());
        Assertions.assertEquals(expectedStatus, t1.getTaskStatus());
    }

    @Test //Все подзадачи со статусом NEW.
    public void StatusShouldBeNewIfAllSubTaskStatusesAreNew() {

        taskManagerTest.createSubTask(id, "Уборка в гостинной", TemplateTask.TaskStatus.NEW);
        int subId = taskManagerTest.createSubTask
                (id, "Уборка в ванной", TemplateTask.TaskStatus.IN_PROGRESS).getSubTaskId();
        expectedStatus = TemplateTask.TaskStatus.IN_PROGRESS;
        Assertions.assertEquals(expectedStatus, taskManagerTest.getTaskById(id).getTaskStatus());

        taskManagerTest.getSubTaskById(subId).setTaskStatus(TemplateTask.TaskStatus.NEW);
        taskManagerTest.updateTaskStatus(id);

        expectedStatus = TemplateTask.TaskStatus.NEW;
        Assertions.assertEquals(expectedStatus, taskManagerTest.getTaskById(id).getTaskStatus());
    }

    @Test //Все подзадачи со статусом DONE.
    public void StatusShouldBeDoneIfAllSubTaskStatusesAreDone() {
        taskManagerTest.createSubTask(id, "Уборка в гостинной", TemplateTask.TaskStatus.DONE);
        taskManagerTest.createSubTask(id, "Уборка в ванной", TemplateTask.TaskStatus.DONE);

        expectedStatus = TemplateTask.TaskStatus.DONE;
        Assertions.assertEquals(expectedStatus, taskManagerTest.getTaskById(id).getTaskStatus());
    }

    @Test //Подзадачи со статусами NEW и DONE.
    public void StatusShouldBeInProgressIfSomeSubTasksDoneAndSomeAreNew() {
        taskManagerTest.createSubTask(id, "Уборка в гостинной", TemplateTask.TaskStatus.DONE);
        taskManagerTest.createSubTask(id, "Уборка в ванной", TemplateTask.TaskStatus.NEW);

        expectedStatus = TemplateTask.TaskStatus.IN_PROGRESS;
        Assertions.assertEquals(expectedStatus, taskManagerTest.getTaskById(id).getTaskStatus());
    }

   @Test //Подзадачи со статусом IN_PROGRESS.
   public void StatusShouldBeInProgressIfAnyOfSubTaskIsInProgress() {
       taskManagerTest.createSubTask(id, "Уборка в гостинной", TemplateTask.TaskStatus.NEW);
       Assertions.assertEquals(expectedStatus, taskManagerTest.getTaskById(id).getTaskStatus());

       taskManagerTest.createSubTask(id, "Уборка в ванной", TemplateTask.TaskStatus.IN_PROGRESS);

       expectedStatus = TemplateTask.TaskStatus.IN_PROGRESS;
       Assertions.assertEquals(expectedStatus, taskManagerTest.getTaskById(id).getTaskStatus());

   }

}