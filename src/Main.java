// Спасибо, все понятно. Часть моментов про static и про магические числа как раз в 4 спринте раскрывают.

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DataManager dataManager = new DataManager();
        Scanner scanner = new Scanner(System.in);

    //создали объект вручную и добавили его в ХТ в которой хранятся все объекты
       // Task task = new Task("Переезд", "Переезжаем в новый дом", true, 1);
       // dataManager.allTasksList.put(task.taskId, task);
       // dataManager.existingIdList.add(task.taskId);

    //тестируем обращение к полям созданного объекта
        /*
        System.out.println("Задача: "+task.taskName);
        System.out.println("Описание задачи: "+task.taskDescription);
        System.out.println("Id задачи: "+task.taskId);
        System.out.println("Статус задачи: "+task.taskStatus);
        System.out.println("Эпик или нет: "+task.isEpic);
        System.out.println("Существующий список Id: "+DataManager.existingIdList);
        */


    //создаем задачи методом
        Task task1 = dataManager.createTask("Ремонт", "Делаем ремонт к праздникам", false, 1);
        dataManager.createTask("Отдых", "Короткий отдых", false, 1);
        dataManager.createTask("Игры", "Играем в разные игры", true, 2);

    //тестируем обращение к полям объекта, созданного методом
        /*
        System.out.println("Задача: "+task1.taskName);
        System.out.println("Описание задачи: "+task1.taskDescription);
        System.out.println("Id задачи: "+task1.taskId);
        System.out.println("Статус задачи: "+ task1.taskStatusList.get(task1.taskStatus));
        System.out.println("Эпик или нет: "+task1.isEpic);
        */

    //тестируем создание пустой задачи,через конструктор без параметров
        /*
        System.out.println("Существующий список Id: " + dataManager.existingIdList);
        System.out.println("Список сохраненных задач: " + dataManager.allTasksList);
        dataManager.createEmptyTask();
        System.out.println("Существующий список Id: " + dataManager.existingIdList);
        System.out.println("Список сохраненных задач: " + dataManager.allTasksList);
        */

    //тестируем вывод информации о задаче по ее номеру
        /*
        System.out.println("Одна из сохраненных задач: "+dataManager.allTasksList.get(scanner.nextInt()));
        System.out.println("Одна из сохраненных задач: "+dataManager.getTaskById(scanner.nextInt()));
        System.out.println (dataManager.getTaskById(dataManager.existingIdList.get(3)));

        System.out.println("Введите ID задачи для отображения.");
        System.out.println("Доступные задачи:"+dataManager.allTasksList.keySet());
        int idNumber = scanner.nextInt();
        if (dataManager.getTaskById(idNumber)==null) {
            System.out.println(Errors.nonExistingTaskId);
        }
                else System.out.println (dataManager.getTaskById(idNumber));
        */

    // тестируем вывод информации о задаче по ее имени
       /*
        System.out.println(dataManager.getTaskByName("ремонт"));
        //этот метод возвращает объект соответствеено его можно присвоить уже тут объекту, если нужно с ним поработать
        Task test = dataManager.getTaskByName("ремонт");

        if (dataManager.getTaskByName("ремонт")==null) {
            System.out.println(Errors.nonExistingTaskName);
          };
        */

    // тестируем обновление задачи = нужно присвоить задаче нужный ID (тут принудительно сделано, но можно и вводить или принимать)
    //и передаем в метод новый объект, но с тем же ID, по этому ID объект перезаписывается в ХТ с задачами
        /*
        Task task5 = new Task("НовыйПереезд", "Переезжаем заново",false);
        task5.taskId = dataManager.existingIdList.get(0);
        //task5.taskId = 16;
        System.out.println(dataManager.updateTaskById(task5));
        System.out.println("Список сохраненных задач: "+ dataManager.allTasksList);
        */

    //тестируем удаление всех объектов и одного по номеру ID
         /* dataManager.removeAllTasks();
         System.out.println(dataManager.removeTaskById(scanner.nextInt()));
         System.out.println("Существующий список Id: "+dataManager.existingIdList);
         System.out.println("Список сохраненных задач: "+ dataManager.allTasksList);
         */

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

    //тестируем удаление всех подзадач для конкретной задачи
        /*
        System.out.println("Выводим все подзадачи по ID " + dataManager.getSubTasksListByTaskId(1));
        System.out.println(dataManager.allTasksList.get(1).isEpic);
        dataManager.deleteAllSubtasksByTaskId(1);
        System.out.println("Выводим все подзадачи по ID " + dataManager.getSubTasksListByTaskId(1));
        System.out.println(dataManager.allTasksList.get(1).isEpic);
        System.out.println ("Выводим задачу по ID " + dataManager.getTaskById(2));
        */

    //тестируем удаление подзадачи по ее номеру
        /*
        System.out.println("Выводим все подзадачи по ID " + dataManager.getSubTasksListByTaskId(2));
        System.out.println(dataManager.getSubTaskIdByName("собрать вещи"));
        System.out.println(dataManager.allSubTasksList);
        System.out.println(dataManager.deleteSubtaskByItsId(1));
        System.out.println(dataManager.allSubTasksList);
        */

    //тестируем удаление подзадачи по ее имени и изменение статуса основной задачи при удалении подзадачи
        /*
        System.out.println("Выводим все подзадачи: " + dataManager.allSubTasksList);
        System.out.println(dataManager.deleteSubtaskByItsName("Купить еды"));
        System.out.println("Выводим все подзадачи: " + dataManager.allSubTasksList);

        System.out.println ("Выводим задачу по ID " + dataManager.getTaskById(1));
        System.out.println("Выводим все подзадачи по ID " + dataManager.getSubTasksListByTaskId(1));
        System.out.println(dataManager.deleteSubtaskByItsName("танцевать"));

        System.out.println("Выводим все подзадачи по ID " + dataManager.getSubTasksListByTaskId(1));
        System.out.println ("Выводим задачу по ID " + dataManager.getTaskById(1));

        System.out.println(dataManager.deleteSubtaskByItsName("валять дурака"));
        System.out.println("Выводим все подзадачи по ID " + dataManager.getSubTasksListByTaskId(1));
        System.out.println ("Выводим задачу по ID " + dataManager.getTaskById(1));
        */

        //тестируем получение списка и вывод только эпик задач
        /*
        System.out.println("Список сохраненных задач: "+ DataManager.allTasksList);
        System.out.println(dataManager.getAllEpicTasks());
        DataManager.allTasksList.get(0).isEpic = false;
        System.out.println(dataManager.getAllEpicTasks());
        DataManager.allTasksList.get(0).isEpic = true;
        System.out.println(dataManager.getAllEpicTasks());
        */

    //тестируем получение списка и вывод только не-эпик задач
        /*
        System.out.println("Список сохраненных задач: "+ dataManager.allTasksList);
        System.out.println(dataManager.getAllNonEpicTasks());
        dataManager.allTasksList.get(0).isEpic = false;
        System.out.println(dataManager.getAllNonEpicTasks());
        */

    //тестируем получение списка и вывод эпик задач и сразу же подзадач к ним
        /*
        System.out.println();
        System.out.println(dataManager.getAllEpicTasks());
        System.out.println(dataManager.getAllEpicTasksAndSubtasks());
        */

    //тестируем метод проверки статуса задачи в зависимости от статуса подзадач
        /*
        System.out.println();
        System.out.println(dataManager.allTasksList.get(1));
        System.out.println(dataManager.getSubTasksListByTaskId(1));
        System.out.println(dataManager.updateTaskStatus(1));
        System.out.println(dataManager.allTasksList.get(1));
        */

    //тестируем обновление статуса задачи при ее обновлении (статус обновляется в зависимости от статуса подзадач)
       /*
        System.out.println();
        System.out.println("Список сохраненных задач: " + dataManager.getAllTasksList());
        System.out.println("Список сохраненных ID задач: " + dataManager.getExistingIdList());

        if (dataManager.createSubTask(0, "Обновляемся 1", 1) == null) {
            System.out.println(Errors.nonExistingTaskId);
        }

        if (dataManager.createSubTask(0, "Обновляемся 2", 1) == null) {
            System.out.println(Errors.nonExistingTaskId);
        }

        System.out.println(dataManager.getSubTasksListByTaskId(0));
        System.out.println("Список сохраненных задач: " + dataManager.getAllTasksList());

        dataManager.createTask("FFF", "Играем", true, 1);
        dataManager.createTask("NNN", "в разные игры", true, 1);

        System.out.println("Список сохраненных задач: " + dataManager.getAllTasksList());
        System.out.println("Список сохраненных ID задач: " + dataManager.getExistingIdList());

        Task task5 = new Task("Новый Переезд", "Переезжаем заново", false, 2);
        //task5.taskId = 0;
        System.out.println(dataManager.updateTaskById(task5,0));

        System.out.println("Список сохраненных ID задач: " + dataManager.getExistingIdList());
        System.out.println("Список всех сохраненных задач: " + dataManager.getAllTasksList());

        System.out.println(dataManager.getEpicTaskById(0));

        System.out.println("Список только эпик задач: "+ dataManager.getAllEpicTasksAndSubtasks());
        System.out.println("Список не эпик задач: "+dataManager.getAllNonEpicTasks());
       */

        //тестируем замену (обновление, удаление) подзадач по номеру id
       /*
        for (SubTask sub: dataManager.getAllSubTasksList()) {
            System.out.println(sub.getSubTaskId()+"  " +sub);
        }
        System.out.println(dataManager.updateSubTask(new SubTask(1,"Серьезно потрудиться",2),4));
        System.out.println();
        for (SubTask sub: dataManager.getAllSubTasksList()) {
            System.out.println(sub.getSubTaskId()+"  " +sub);
        }

        System.out.println(dataManager.getAllSubTasksList());
        System.out.println(dataManager.deleteSubtaskByItsId(15));
        System.out.println(dataManager.getAllSubTasksList());
        System.out.println(dataManager.getSubTaskById(0));
        System.out.println(dataManager.getSubTaskIdByName("купить еды"));
        System.out.println(dataManager.getSubTaskNameById(31));
        */

    }
}