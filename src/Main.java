import HTTP.HttpTaskServer;
import HTTP.KVServer;
import HTTP.KVTaskClient;
import Manager.*;
import Model.SubTask;
import Model.Task;
import Model.TemplateTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefault();

        //---------------------------------------------------------
        //Проверяем запись задач/подзадач и истории в файлы
        FileBackedTasksManager taskManagerBackup =
             (FileBackedTasksManager) Managers.getManagerWithBackup("C:\\Users\\Вуня\\Desktop\\dev\\6th sprint\\java-kanban\\src\\DataStorage");
    //    FileBackedTasksManager taskManagerBackup =
      //          (FileBackedTasksManager) Managers.getManagerWithBackup(
        //              "C:\\Users\\User\\Desktop\\учеба\\Дж\\8 sp\\6th sprint\\java-kanban\\src\\DataStorage");


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
        // File file = new File("C:\\Users\\User\\Desktop\\учеба\\Дж\\8 sp\\6th sprint\\java-kanban\\src\\DataStorage");
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


//-----Дата и время
        for (Task t: taskManagerBackup.getAllTasksList().values()) {
            System.out.println("Время создания задачи "+t.getTaskId()+": "+t.getTaskCreationTime());
            for (SubTask s: t.getSubTasksList()) {
                System.out.println("Время создания подзадачи "+s.getTaskId()+": "+s.getTaskCreationTime());
            }
        }
        System.out.println("Время создания: "+taskManagerBackup.getAllTasksList().get(1).getTaskCreationTime());
        System.out.println("Время начала: "+taskManagerBackup.getAllTasksList().get(1).getTaskStartTime());
        System.out.println("Продолжительность: "+taskManagerBackup.getAllTasksList().get(1).getTaskDuration().toMinutes());
        System.out.println("Время окончания: "+taskManagerBackup.getAllTasksList().get(1).getTaskEndTime());
        System.out.println();


        taskManagerBackup.getSubTaskById(13).setTaskCreationTime(taskManagerBackup.getSubTaskById(7).getTaskCreationTime());
        taskManagerBackup.getSubTaskById(12).setTaskCreationTime(taskManagerBackup.getSubTaskById(7).getTaskCreationTime().minusSeconds(5));

        System.out.println(taskManagerBackup.getTaskById(1).getSubTasksList());

        System.out.println();
        System.out.println("Время начала "+taskManagerBackup.getTaskById(1).getTaskStartTime());
        System.out.println("Время окончания "+ taskManagerBackup.getTaskById(1).getTaskEndTime());

        System.out.println(TimeOptimizer.organizeSubTasksTime(taskManagerBackup.getTaskById(1)));

        System.out.println(taskManagerBackup.getTaskById(1).getTaskStartTime());
        for (SubTask s: taskManagerBackup.getTaskById(1).getSubTasksList()) {
            System.out.println(s.getSubTaskId() +": "+s.getTaskCreationTime() + " время начала " + s.getTaskStartTime() + " продолжительность " + s.getTaskDuration().toMinutes() + " время окончания " + s.getTaskEndTime());
        }


        taskManagerBackup.setTaskStartTime(1,taskManagerBackup.getTaskById(1).getTaskStartTime().plusSeconds(1200));//задали для таска новое время начала; внутри метода timeOptimizer перетряхнул все сабтаски с учтом того что время начала куда-то изменилось
        System.out.println("Время начала "+taskManagerBackup.getTaskById(1).getTaskStartTime()); //убедились что время начала сдвинулось
        for (SubTask s: taskManagerBackup.getTaskById(1).getSubTasksList()) {
            System.out.println(s.getSubTaskId() +": "+s.getTaskCreationTime() + " время начала " + s.getTaskStartTime() + " продолжительность " + s.getTaskDuration().toMinutes() + " время окончания " + s.getTaskEndTime());
        }

        System.out.println("Время окончания "+ taskManagerBackup.getTaskById(1).getTaskEndTime()); //проверили что время окончания соответствует времени окончания последнего сабтаска

        System.out.println();


        //проверяем отработку метода сортировки и метода вывода задач/подзадач  одну за другой без пересечений
       TimeOptimizer.printTasksSchedule((HashMap<Integer, Task>) taskManagerBackup.getAllTasksList());
       taskManagerBackup.getAllTasksList().get(1).setTaskStartTime(Instant.now().plusSeconds(100000));
       taskManagerBackup.setSubTaskStartTime(1, 7, Instant.now().plusSeconds(120000));
       taskManagerBackup.setAllTasksList(TimeOptimizer.organizeAllTasksTime((HashMap<Integer, Task>) taskManagerBackup.getAllTasksList()));
        System.out.println();
        TimeOptimizer.printTasksSchedule((HashMap<Integer, Task>) taskManagerBackup.getAllTasksList());
        System.out.println();

        System.out.println(taskManagerBackup.getAllTasksList().get(1).getSubTasksList().get(3).getTaskEndTime());
    //server part
        HttpTaskServer server = new HttpTaskServer(); //создали сервер
        server.setServerManager(taskManagerBackup); //присвоили менеджеру существующий менеджер чтобы не вводить задачи заново
        server.startServer(); //запустили сервер, там создался экземпляр файлового менеджера - fbtManager


        KVServer kvServer = new KVServer();
        kvServer.start();

        KVTaskClient client = new KVTaskClient("http://localhost:8078/"); //конструктор для нового пользователя у кторого нет apiToken

        KVTaskClient client1 = new KVTaskClient("API_TOKEN=DEBUG", "http://localhost:8078/"); //конструктор для тех у кого есть apiToken
        System.out.println(client1.getApiToken());

        String key = String.valueOf(6);
        String key1 = String.valueOf(7);

        client1.put(key,"{\"user\":\"Вася\",\"text\":\"Ништяк\"}"); //проверили запись
        client1.put(key1,"{\"user\":\"Петя\",\"text\":\"Супер\"}");

        client1.load(key); //проверили загрузку
        client1.load(key1);

        client1.put(key1,"{\"user\":\"НеПетя\",\"text\":\"Нормас\"}"); //проверили перезапись
        client1.load(key1);


    }


}