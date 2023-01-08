//Уважаемый Евгений, все, что в мэйне прописано это часть тестирования, которое я осуществлял в процессе написания,
//то есть прошу не рассматривать мэйн в качестве кода на ревью.
// Оставил на тот случай если вдруг это чем-то облегчит Вам работу. Спасибо.

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

    //создали объект вручную и добавили его в ХТ в которой хранятся все объекты
        Task task = new Task("Переезд", "Переезжаем в новый дом", true, 1);
        DataManager.allTasksList.put(task.taskId, task);

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
        Task task1 = DataManager.createTask("Ремонт", "Делаем ремонт к праздникам", false, 1);
        DataManager.createTask("Отдых", "Короткий отдых", false, 1);
        DataManager.createTask("Игры", "Играем в разные игры", true, 2);

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
        System.out.println("Существующий список Id: " + DataManager.existingIdList);
        System.out.println("Список сохраненных задач: " + DataManager.allTasksList);
        DataManager.createEmptyTask();
        System.out.println("Существующий список Id: " + DataManager.existingIdList);
        System.out.println("Список сохраненных задач: " + DataManager.allTasksList);
        */

    //тестируем вывод информации о задаче по ее номеру
        /*
        System.out.println("Одна из сохраненных задач: "+DataManager.allTasksList.get(scanner.nextInt()));
        System.out.println("Одна из сохраненных задач: "+DataManager.getTaskById(scanner.nextInt()));
        System.out.println (DataManager.getTaskById(DataManager.existingIdList.get(3)));

        System.out.println("Введите ID задачи для отображения.");
        System.out.println("Доступные задачи:"+DataManager.allTasksList.keySet());
        int idNumber = scanner.nextInt();
        if (DataManager.getTaskById(idNumber)==null) {
            System.out.println(Errors.error65);
        }
                else System.out.println (DataManager.getTaskById(idNumber));
        */

    // тестируем вывод информации о задаче по ее имени
       /*
        System.out.println(DataManager.getTaskByName("ремонт"));
        //этот метод возвращает объект соответствеено его можно присвоить уже тут объекту, если нужно с ним поработать
        Task test = DataManager.getTaskByName("ремонт");

        if (DataManager.getTaskByName("ремонт")==null) {
            System.out.println(Errors.error77);
          };
        */

    // тестируем обновление задачи = нужно присвоить задаче нужный ID (тут принудительно сделано, но можно и вводить или принимать)
    //и передаем в метод новый объект, но с тем же ID, по этому ID объект перезаписывается в ХТ с задачами
        /*
        Task task5 = new Task("НовыйПереезд", "Переезжаем заново",false);
        task5.taskId = DataManager.existingIdList.get(0);
        //task5.taskId = 16;
        System.out.println(DataManager.updateTaskById(task5));
        System.out.println("Список сохраненных задач: "+ DataManager.allTasksList);
        */

    //тестируем удаление всех объектов и одного по номеру ID
         /* DataManager.removeAllTasks();
         System.out.println(DataManager.removeTaskById(scanner.nextInt()));
         System.out.println("Существующий список Id: "+DataManager.existingIdList);
         System.out.println("Список сохраненных задач: "+ DataManager.allTasksList);
         */

    //______________________разбираемся с подзадачами___________________________________________
        System.out.println();
        SubTask subTask = DataManager.createSubTask(2, "Собрать вещи", 1);
        // System.out.println(DataManager.createSubTask(1,"Собрать вещи",1));
        SubTask subTask1 = DataManager.createSubTask(2, "Вызвать работников", 1);
        // System.out.println(subTask);
        // System.out.println(subTask1);

        DataManager.createSubTask(1, "Купить еды", 1);
        DataManager.createSubTask(1, "Танцевать", 1);
        DataManager.createSubTask(1, "Валять дурака", 1);

        System.out.println(DataManager.allSubTasksList);
        System.out.println("Уникальный номер подзадачи " + DataManager.getSubTaskIdByName("Валять дурака"));
        System.out.println(DataManager.getSubTaskNameById(1));

   //тестируем получение по ID, задачи, подзадач и всего эпика
        System.out.println("Выводим задачу по ID " + DataManager.getTaskById(1));
        System.out.println("Выводим все подзадачи по ID " + DataManager.getSubTasksListByTaskId(1));
        System.out.println("А это эпик, выводим и задачи и подзадачи по ID: " + DataManager.getEpicTaskById(1));

    //тестируем удаление всех подзадач для конкретной задачи
        /*
        System.out.println("Выводим все подзадачи по ID " + DataManager.getSubTasksListByTaskId(1));
        System.out.println(DataManager.allTasksList.get(1).isEpic);
        DataManager.deleteAllSubtasksByTaskId(1);
        System.out.println("Выводим все подзадачи по ID " + DataManager.getSubTasksListByTaskId(1));
        System.out.println(DataManager.allTasksList.get(1).isEpic);
        System.out.println ("Выводим задачу по ID " + DataManager.getTaskById(2));
        */

    //тестируем удаление подзадачи по ее номеру
        /*
        System.out.println("Выводим все подзадачи по ID " + DataManager.getSubTasksListByTaskId(2));
        System.out.println(DataManager.getSubTaskIdByName("собрать вещи"));
        System.out.println(DataManager.allSubTasksList);
        System.out.println(DataManager.deleteSubtaskByItsId(1));
        System.out.println(DataManager.allSubTasksList);
        */

    //тестируем удаление подзадачи по ее имени и изменение статуса основной задачи при удалении подзадачи
        /*
        System.out.println("Выводим все подзадачи: " + DataManager.allSubTasksList);
        System.out.println(DataManager.deleteSubtaskByItsName("Купить еды"));
        System.out.println("Выводим все подзадачи: " + DataManager.allSubTasksList);

        System.out.println ("Выводим задачу по ID " + DataManager.getTaskById(1));
        System.out.println("Выводим все подзадачи по ID " + DataManager.getSubTasksListByTaskId(1));
        System.out.println(DataManager.deleteSubtaskByItsName("танцевать"));

        System.out.println("Выводим все подзадачи по ID " + DataManager.getSubTasksListByTaskId(1));
        System.out.println ("Выводим задачу по ID " + DataManager.getTaskById(1));

        System.out.println(DataManager.deleteSubtaskByItsName("валять дурака"));
        System.out.println("Выводим все подзадачи по ID " + DataManager.getSubTasksListByTaskId(1));
        System.out.println ("Выводим задачу по ID " + DataManager.getTaskById(1));
        */

    //тестируем замену (обновление) подзадачи
        SubTask subTest = DataManager.createSubTask(1, "Варим пиво", 3);
        System.out.println(DataManager.allSubTasksList);
        SubTask subTest1 = new SubTask(1, "Варим пиво", 1);
        //  subTest1.mainTaskId = 1;
        //  subTest1.subTaskName = "Варим пиво";
        //  subTest1.subTaskStatus = 1;

        System.out.println(DataManager.updateSubTask(subTest1));
        System.out.println(DataManager.allSubTasksList);

    //тестируем получение списка и вывод только эпик задач
        /*
        System.out.println("Список сохраненных задач: "+ DataManager.allTasksList);
        System.out.println(DataManager.getAllEpicTasks());
        DataManager.allTasksList.get(0).isEpic = false;
        System.out.println(DataManager.getAllEpicTasks());
        DataManager.allTasksList.get(0).isEpic = true;
        System.out.println(DataManager.getAllEpicTasks());
        */

    //тестируем получение списка и вывод только не-эпик задач
        /*
        System.out.println("Список сохраненных задач: "+ DataManager.allTasksList);
        System.out.println(DataManager.getAllNonEpicTasks());
        DataManager.allTasksList.get(0).isEpic = false;
        System.out.println(DataManager.getAllNonEpicTasks());
        */

    //тестируем получение списка и вывод эпик задач и сразу же подзадач к ним
        /*
        System.out.println();
        System.out.println(DataManager.getAllEpicTasks());
        System.out.println(DataManager.getAllEpicTasksAndSubtasks());
        */

    //тестируем метод проверки статуса задачи в зависимости от статуса подзадач
        /*
        System.out.println();
        System.out.println(DataManager.allTasksList.get(1));
        System.out.println(DataManager.getSubTasksListByTaskId(1));
        System.out.println(DataManager.updateTaskStatus(1));
        System.out.println(DataManager.allTasksList.get(1));
        */

    //тестируем обновление статуса задачи при ее обновлении (статус обновляется в зависимости от статуса подзадач)
        System.out.println();
        System.out.println("Список сохраненных задач: " + DataManager.allTasksList);
        System.out.println("Тест. Список Id " + DataManager.existingIdList);

        if (DataManager.createSubTask(0, "Обновляемся 1", 1) == null) {
            System.out.println(Errors.error65);
        }

        if (DataManager.createSubTask(0, "Обновляемся 2", 1) == null) {
            System.out.println(Errors.error65);
        }

        System.out.println(DataManager.getSubTasksListByTaskId(0));

        Task task5 = new Task("Новый Переезд", "Переезжаем заново", false, 2);
        task5.taskId = 0;

        System.out.println(DataManager.updateTaskById(task5));
        System.out.println("Список сохраненных задач: " + DataManager.allTasksList);

    }
}