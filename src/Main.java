import Interfaces.TaskManager;
import Manager.InMemoryHistoryManager;
import Manager.InMemoryTaskManager;
import Manager.Managers;
import Model.SubTask;
import Model.Task;
import Model.TemplateTask;

public class Main {
    public static void main(String[] args) {

        // InMemoryTaskManager taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

        // InMemoryTaskManager dataManager = new InMemoryTaskManager(Managers.getDefaultHistory());

        InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefault();

        // TaskManager taskManager = Managers.getDefault();

        taskManager.createNewTask();

        taskManager.createNewTask(new Task());
        taskManager.createNewTask(new Task("Новый год", "Купить подарки"));
        System.out.println(taskManager.getAllTasksList());

        taskManager.deleteTaskById(3);
        System.out.println(taskManager.getAllTasksList());

        SubTask sub = new SubTask("Ремонт", TemplateTask.TaskStatus.DONE);
        SubTask sub1 = new SubTask();
        System.out.println(sub1);

        taskManager.createSubTask(1);
        // taskManager.createSubTask(1, "Собираем хлам", TemplateClass.TaskStatus.IN_PROGRESS);
        taskManager.createSubTask(1, new SubTask());
        System.out.println(taskManager.createSubTask(2, new SubTask("Собираем хлам", TemplateTask.TaskStatus.IN_PROGRESS)));

        System.out.println(taskManager.getAllTasksList().get(1));
        System.out.println(taskManager.getTaskById(2));

        System.out.println(taskManager.getSubTaskById(8));

        System.out.println("Выводим все существующие подзадачи: " + taskManager.getAllExistingSubtasks());

        System.out.println("Выводим подзадачи для задачи по ID: " + taskManager.getSubtasksForCertainTaskByID(2));

        System.out.println("Выводим все эпики: " + taskManager.getAllEpicTasksList());
        taskManager.createNewTask();
        System.out.println("Выводим все не эпики: " + taskManager.getAllNonEpicTasksList());

        //замена подзадачи по ее id
        System.out.println(taskManager.getSubTaskById(8));
        System.out.println(taskManager.updateSubTask(new SubTask("Подзадача на замену", TemplateTask.TaskStatus.IN_PROGRESS), 8));
        System.out.println(taskManager.getSubTaskById(8));

        //удаление всех задач
        // System.out.println(taskManager.getAllTasksList());
        // taskManager.deleteAllTasks();
        // System.out.println(taskManager.getAllTasksList());

        //удаление подзадачи по ее ID
        //System.out.println(taskManager.getTaskById(2));
        //System.out.println(taskManager.deleteSubTaskByID(8));
        System.out.println(taskManager.getSubTaskById(8));

        System.out.println(taskManager.getTaskById(2));
        taskManager.deleteAllSubTasksByTaskId(2);
        System.out.println(taskManager.getTaskById(2));

        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.changeTaskNameById(1, "Готовим ужин"));
        System.out.println(taskManager.changeTaskDescriptionById(1, "Готовим пельмени на ужин"));
        System.out.println(taskManager.getTaskById(1));

        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.changeNonEpicTaskStatusById(2, TemplateTask.TaskStatus.DONE));
        System.out.println(taskManager.getTaskById(2));


        taskManager.deleteSubTaskByID(6);
        taskManager.deleteSubTaskByID(7);

        taskManager.createSubTask(1, new SubTask("Лепим пельмени", TemplateTask.TaskStatus.DONE));
        taskManager.createSubTask(1, new SubTask("Варим пельмени", TemplateTask.TaskStatus.DONE));
        taskManager.createSubTask(1, new SubTask("Едим пельмени", TemplateTask.TaskStatus.DONE));
        System.out.println(taskManager.getTaskById(1));

        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getHistory().getHistoryList());

        taskManager.getHistory().getHistoryMap().clear();
        System.out.println("Очистили историю " + taskManager.getHistory().getHistoryList());

        //проверка что в истории только один раз остается запись при многократном обращении к объекту
        for (int i = 1; i <= 3; i++) {
            taskManager.getSubTaskById(11);
        }
        System.out.println(taskManager.getHistory().getHistoryList());

        //проверка что добавление идет в конец истории
        taskManager.getSubTaskById(12);
        System.out.println(taskManager.getHistory().getHistoryList());

        //проверка что объект, который уже есть в истории, при повторном вызове перезаписывается в конец
        taskManager.getSubTaskById(11);
        System.out.println(taskManager.getHistory().getHistoryList());

        //проверка отсутствия повторов и перезаписи в конец при повторном просмотре
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(11);
        taskManager.getSubTaskById(12);
        taskManager.getSubTaskById(13);
        System.out.println();

        for (TemplateTask t : taskManager.getHistory().getHistoryList()) {
            System.out.println(t);
        }
/*
        //проверика, что при удалении из истории эпик задачи также удаляются записи о подзадачах
        System.out.println();
        taskManager.getHistory().removeFromHistory(1);

        for (TemplateTask t : taskManager.getHistory().getHistoryList()) {
            System.out.println(t);
        }

        //проверка метода удаления из истории
        System.out.println();
        taskManager.getHistory().removeFromHistory(2);

        for (TemplateTask t : taskManager.getHistory().getHistoryList()) {
            System.out.println(t);
        }

        //проверка что при удалении самой задачи запись о ней и о подзадачах также из истории уходят
     taskManager.deleteTaskById(2);
     //taskManager.deleteTaskById(2);
     System.out.println();
     for (TemplateTask t : taskManager.getHistory().getHistoryList()) {
      System.out.println(t);
     }

    //проверка что при удалении подзадачи запись о ней также из истории уходит
     taskManager.deleteSubTaskByID(12);
     System.out.println();
     for (TemplateTask t : taskManager.getHistory().getHistoryList()) {
      System.out.println(t);
           }

        */
    }
}