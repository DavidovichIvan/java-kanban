
import Interfaces.TaskManager;
import Manager.InMemoryTaskManager;
import Manager.Managers;
import Model.SubTask;
import Model.Task;
import Model.TemplateTask;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager dataManager = new InMemoryTaskManager();
        //InMemoryTaskManager dataManager = (InMemoryTaskManager)Managers.getDefault();

        Task task1 = dataManager.createTask("Ремонт", "Делаем ремонт к праздникам", false, 1);
        dataManager.createTask("Отдых", "Короткий отдых", false, 1);
        dataManager.createTask("Игры", "Играем в разные игры", true, 2);

    //______________________разбираемся с подзадачами___________________________________________
        System.out.println();
        SubTask subTask = dataManager.createSubTask(2, "Собрать вещи", 1);
        // System.out.println(dataManager.createSubTask(1,"Собрать вещи",1));
        SubTask subTask1 = dataManager.createSubTask(2, "Вызвать работников", 1);
        // System.out.println(subTask);
        // System.out.println(subTask1);

        dataManager.createSubTask(1, "Купить еды", 1);
        dataManager.createSubTask(1, "Танцевать", 1);
        dataManager.createSubTask(1, "Валять дурака", 1);

        System.out.println(dataManager.getAllSubTasksList());
        System.out.println("Уникальный номер подзадачи " + dataManager.getSubTaskIdByName("Валять дурака"));
        System.out.println(dataManager.getSubTaskNameById(1));

        //тестируем получение по ID, задачи, подзадач и всего эпика
        System.out.println("Выводим задачу по ID " + dataManager.getTaskById(1));
        System.out.println("Выводим все подзадачи по ID " + dataManager.getSubTasksListByTaskId(1));
        System.out.println("А это эпик, выводим и задачи и подзадачи по ID: " + dataManager.getEpicTaskById(2));

        //тестируем сохранение и просмотр истории просмотров
/*
        System.out.println("История просмотров "+dataManager.getHistory().getHistoryList());
        System.out.println("История просмотров "+dataManager.getHistory().getHistoryList());
        dataManager.getTaskById(1);
        System.out.println("История просмотров "+dataManager.getHistory().getHistoryList());
        dataManager.getSubTaskById(1);
        System.out.println("История просмотров "+dataManager.getHistory().getHistoryList());
        dataManager.getEpicTaskById(1);
        System.out.println("История просмотров "+dataManager.getHistory().getHistoryList());
        System.out.println("История просмотров "+dataManager.getHistory().getHistoryList());
        dataManager.getTaskById(0);
        System.out.println("История просмотров "+dataManager.getHistory().getHistoryList());
        dataManager.getTaskById(1);
        System.out.println("История просмотров "+dataManager.getHistory().getHistoryList());
        System.out.println("Элемент из списка истории: "+dataManager.getHistory().getHistoryList());

        for (int i=1; i<=10;i++) {
            dataManager.getTaskById(0);
            System.out.println("Это запись номер "+ i +dataManager.getHistory().getHistoryList());
        }
        dataManager.getTaskById(1);
        System.out.println(dataManager.getHistory().getHistoryList());
        dataManager.getTaskById(1);
        System.out.println(dataManager.getTaskById(2));
        System.out.println(dataManager.getHistory().getHistoryList());
        System.out.println(dataManager.getHistory().getHistoryList().size());

        for (int i=0; i<10;i++) {
            System.out.println("Это запись номер "+ i +" "+dataManager.getHistory().getHistoryList().get(i));
        }
        dataManager.getTaskById(0);
        System.out.println();
        for (int i=0; i<10;i++) {
            System.out.println("Это запись номер "+ i +" "+dataManager.getHistory().getHistoryList().get(i));
        }

        //тестируем возврат объекта с интерфейсом TaskManager
        System.out.println(dataManager);
        System.out.println(Managers.getDefault());
        System.out.println("Была история "+ dataManager.getHistory().getHistoryList());
        dataManager.getHistory().getHistoryList().clear();
        System.out.println("Очистили историю "+ dataManager.getHistory().getHistoryList());

        dataManager.getEpicTaskById(1);
        System.out.println(dataManager.getHistory().getHistoryList());
        System.out.println(dataManager.getHistory().getHistoryList().size());

        for (int i = 0; i< dataManager.getHistory().getHistoryList().size(); i++) {
            System.out.println("Это запись номер "+ i +" "+ dataManager.getHistory().getHistoryList().get(i));
        }

        for (int i=1; i<=10;i++) {
            dataManager.getTaskById(0);
                   }
        for (int i = 0; i< dataManager.getHistory().getHistoryList().size(); i++) {
            System.out.println("Это запись номер "+ i +" "+ dataManager.getHistory().getHistoryList().get(i));
        }

        dataManager.getEpicTaskById(1);
        dataManager.getTaskById(2);
        dataManager.getSubTaskById(2);

        System.out.println();
        for (int i = 0; i< dataManager.getHistory().getHistoryList().size(); i++) {
            System.out.println("Это запись номер "+ (i+1) +" "+ dataManager.getHistory().getHistoryList().get(i));
        }

        dataManager.getEpicTaskById(1);
        dataManager.getSubTaskById(1);

        for (int i = 0; i< dataManager.getHistory().getHistoryList().size(); i++) {
            System.out.println("Это запись номер "+ (i+1) +" "+ dataManager.getHistory().getHistoryList().get(i));
        }
    */

    }
}