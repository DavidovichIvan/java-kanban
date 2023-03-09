import Interfaces.TaskManager;
import Manager.*;
import Model.SubTask;
import Model.Task;
import Model.TemplateTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefault();

        //---------------------------------------------------------
        //Проверяем запись задач/подзадач и истории в файлы
        FileBackedTasksManager taskManagerBackup =
                (FileBackedTasksManager) Managers.getManagerWithBackup(
                         "C:\\Users\\Вуня\\Desktop\\dev\\6th sprint\\java-kanban\\src\\DataStorage");

        taskManagerBackup.createNewTask();
        taskManagerBackup.createNewTask(new Task());
        taskManagerBackup.createNewTask(new Task("Новый год", "Купить подарки"));
        System.out.println(taskManagerBackup.getAllTasksList());

        taskManagerBackup.deleteTaskById(3);
        System.out.println(taskManagerBackup.getAllTasksList());

        SubTask sub = new SubTask("Ремонт", TemplateTask.TaskStatus.DONE);
        SubTask sub1 = new SubTask();
        System.out.println(sub1);

        taskManagerBackup.createSubTask(1);
        taskManagerBackup.createSubTask(1, new SubTask());
        System.out.println(taskManagerBackup.createSubTask(2, new SubTask(
                "Собираем хлам", TemplateTask.TaskStatus.IN_PROGRESS)));

        System.out.println(taskManagerBackup.getAllTasksList().get(1));
        System.out.println(taskManagerBackup.getTaskById(2));

        System.out.println(taskManagerBackup.getSubTaskById(8));

        System.out.println("Выводим все существующие подзадачи: " + taskManagerBackup.getAllExistingSubtasks());
        System.out.println("Выводим подзадачи для задачи по ID: " + taskManagerBackup.getSubtasksForCertainTaskByID(2));
        System.out.println("Выводим все эпики: " + taskManagerBackup.getAllEpicTasksList());
        taskManagerBackup.createNewTask();
        System.out.println("Выводим все не эпики: " + taskManagerBackup.getAllNonEpicTasksList());

        //замена подзадачи по ее id
        System.out.println(taskManagerBackup.getSubTaskById(8));
        System.out.println(taskManagerBackup.updateSubTask(
                new SubTask("Подзадача на замену", TemplateTask.TaskStatus.IN_PROGRESS), 8));
        System.out.println(taskManagerBackup.getSubTaskById(8));

        System.out.println(taskManagerBackup.getSubTaskById(8));

        System.out.println(taskManagerBackup.getTaskById(2));
        taskManagerBackup.deleteAllSubTasksByTaskId(2);
        System.out.println(taskManagerBackup.getTaskById(2));

        System.out.println(taskManagerBackup.getTaskById(1));
        System.out.println(taskManagerBackup.changeTaskNameById(1, "Готовим ужин"));
        System.out.println(taskManagerBackup.changeTaskDescriptionById(1, "Готовим пельмени на ужин"));
        System.out.println(taskManagerBackup.getTaskById(1));

        System.out.println(taskManagerBackup.getTaskById(2));
        System.out.println(taskManagerBackup.changeNonEpicTaskStatusById(2, TemplateTask.TaskStatus.DONE));
        System.out.println(taskManagerBackup.getTaskById(2));

        taskManagerBackup.deleteSubTaskByID(6);

        taskManagerBackup.createSubTask(1, new SubTask("Лепим пельмени", TemplateTask.TaskStatus.DONE));
        taskManagerBackup.createSubTask(1, new SubTask("Варим пельмени", TemplateTask.TaskStatus.DONE));
        taskManagerBackup.createSubTask(1, new SubTask("Едим пельмени", TemplateTask.TaskStatus.DONE));
        System.out.println(taskManagerBackup.getTaskById(1));

        System.out.println(taskManagerBackup.getTaskById(2));
        System.out.println(taskManagerBackup.getHistory().getHistoryList());

        taskManagerBackup.getHistory().getHistoryMap().clear();
        System.out.println("Очистили историю " + taskManagerBackup.getHistory().getHistoryList());

        //проверка что в истории только один раз остается запись при многократном обращении к объекту
        for (int i = 1; i <= 3; i++) {
            taskManagerBackup.getSubTaskById(11);
        }
        System.out.println(taskManagerBackup.getHistory().getHistoryList());

        //проверка что добавление идет в конец истории
        taskManagerBackup.getSubTaskById(12);
        System.out.println(taskManagerBackup.getHistory().getHistoryList());

        //проверка что объект, который уже есть в истории, при повторном вызове перезаписывается в конец
        taskManagerBackup.getSubTaskById(11);
        System.out.println(taskManagerBackup.getHistory().getHistoryList());

        System.out.println();

        for (TemplateTask t : taskManagerBackup.getHistory().getHistoryList()) {
            System.out.println(t);
        }

        //---------------------------------------------------------
        System.out.println(taskManagerBackup.getAllTasksList());
        System.out.println(taskManagerBackup.getHistory().getHistoryList());

        taskManagerBackup.getSubTaskById(7);

        System.out.println();
        try {
            System.out.println(Files.readString(Path.of(taskManagerBackup.getDataFilePath())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println();

        taskManagerBackup.getTaskById(1);
        taskManagerBackup.getTaskById(2);
        //-----------------------------------------------------------------------------------------
        //Проверяем создание объекта с восстановлением задач из файла
        File file = new File("C:\\Users\\Вуня\\Desktop\\dev\\6th sprint\\java-kanban\\src\\DataStorage");
        FileBackedTasksManager taskManagerBackup1;

        try {
            taskManagerBackup1 = FileBackedTasksManager.loadFromFile(file);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }

        for (Task t : taskManagerBackup1.getAllTasksList().values()) {
            System.out.println(t);
        }
        System.out.println();

        //Проверяем восстановление истории
        for (TemplateTask t : taskManagerBackup1.getHistory().getHistoryList()) {
            System.out.println(t);
        }
    }
}