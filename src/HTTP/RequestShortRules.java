package HTTP;

/**
 * basic rules for requests being handled by TaskHandler class
 */
public class RequestShortRules {

    final static String REQUEST_RULES_MESSAGE =  "Поступил запрос в некорректном формате.\n\n Запросы должны соответствовать следующему формату:\n" +
          "1. По пути http://localhost:8080/tasks направляются запросы, касающиеся задач\n" +
            "GET - возвращает список всех существующих задач;\n" +
            "DELETE - удаляет все существующие задачи;\n" +
            "POST - добавляет новую задачу.\n\n"+

          "2. По пути http://localhost:8080/tasks/history осуществляется только GET запрос, в ответ на который возвращается история просмотров.\n\n"+

          "3. По пути http://localhost:8080/tasks c параметром methodName " +
            "запросом ACTION инициируется выполнение метода, имя которого указывается в качестве параметра запроса без круглых скобок." +
            "Например: //localhost:8080/tasks?methodName=deleteAllTasks - приведет к вызову метода deleteAllTasks().\"\n\n" +

          "4. По пути http://localhost:8080/tasks/task с обязательным параметром id осуществляются действия с существующими задачами.\n" +
            "Например:\n " +
            "GET по пути  http://localhost:8080/tasks/task?id=1 возвращает данные о задаче с taskId = 1\n" +
            "DELETE по пути  http://localhost:8080/tasks/task?id=2 удаляет задачу с taskId = 2\n" +
            "PATCH по пути  http://localhost:8080/tasks/task?id=3 обновляет задачу с taskId = 3\n\n" +

          "5. По пути http://localhost:8080/tasks/subtasks осуществляется взаимодействие с подзадачами. " +
            "В качестве параметров передаются id основной задачи и subId для подзадачи. В теле запроса передается объект в формате JSON \n" +
            "Например:\n"+
            "GET по пути  http://localhost:8080/tasks/subtasks?subId=1 возвращает данные о подзадаче с SubTaskId = 1.\n"+
            "POST по пути  http://localhost:8080/tasks/subtasks?mainTaskId=2 добавляет новую подзадачу к задаче с id = 2.\n"+
            "DELETE по пути  http://localhost:8080/tasks/subtasks?subId=2 удаляет подзадачу с SubTaskId = 2.\n"+
            "PATCH по пути  http://localhost:8080/tasks/subtasks?subId=4 обновляет подзадачу с SubTaskId 4 .\n\n"

            ;

}
