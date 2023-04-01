/*
Комментарии к ТЗ.

Практический весь новый код в пакете HTTP.

Всевозможные варианты отработки запросов (в том числе предусмотренные кодом негативные сценарии) я прогонял через insomnia
либо через main если это внутрипрограммные клиентские запросы.
Тесты (позитивные) для основных эндпоинтов также через клиентсике запросы в тестовом блоке прописал.

*/
package HTTP;

import Interfaces.TaskManager;
import Manager.Feedback;
import Model.SubTask;
import Model.Task;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_CHARSET;

public class TaskHandler implements HttpHandler {
    protected TaskManager serverManager;

    public TaskHandler(TaskManager serverManager) {
        this.serverManager = serverManager;
    }

    enum Endpoint {
        GET_ALL_TASKS,
        DELETE_ALL_TASKS,
        ADD_NEW_TASK,
        GET_TASK_BY_ID,
        DELETE_TASK_BY_ID,
        UPDATE_TASK_BY_ID,
        START_METHOD_BY_NAME,
        GET_HISTORY,
        CREATE_NEW_SUBTASK,
        GET_SUBTASK,
        DELETE_SUBTASK,
        UPDATE_SUBTASK,
        REQUESTED_ENDPOINT_UNKNOWN
    }

    String response = "";
    Gson gson = new Gson();

    private int rCode = 200;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String requestPath = httpExchange.getRequestURI().getPath();
        String requestPathFull = String.valueOf(httpExchange.getRequestURI());
        Endpoint endpoint = getEndpoint(requestPath, requestPathFull, requestMethod);
        switch (endpoint) {
            case REQUESTED_ENDPOINT_UNKNOWN:
                rCode = 400;
                writeResponse(httpExchange, RequestShortRules.REQUEST_RULES_MESSAGE, rCode);
                break;
            case GET_ALL_TASKS:
                rCode = 200;
                handleGetAllTasks(httpExchange, rCode);
                break;
            case DELETE_ALL_TASKS:
                rCode = 200;
                handleDeleteAllTasks(httpExchange, rCode);
                break;
            case ADD_NEW_TASK:
                rCode = 200;
                handleCreateNewTask(httpExchange, rCode);
                break;
            case GET_TASK_BY_ID:
                rCode = 200;
                handleGetTaskById(httpExchange, rCode);
                break;
            case DELETE_TASK_BY_ID:
                rCode = 200;
                handleDeleteTaskById(httpExchange, rCode);
                break;
            case GET_HISTORY:
                rCode = 200;
                handleGetHistory(httpExchange, rCode);
                break;
            case CREATE_NEW_SUBTASK:
                rCode = 200;
                handleCreateSubTask(httpExchange, rCode);
                break;
            case GET_SUBTASK:
                rCode = 200;
                handleGetSubTask(httpExchange, rCode);
                break;
            case DELETE_SUBTASK:
                rCode = 200;
                handleDeleteSubTask(httpExchange, rCode);
                break;
            case START_METHOD_BY_NAME:
                rCode = 200;
                handleStartMethod(httpExchange, rCode);
                break;
            case UPDATE_TASK_BY_ID:
                rCode = 200;
                handleUpdateTaskById(httpExchange, rCode);
                break;
            case UPDATE_SUBTASK:
                rCode = 200;
                handleUpdateSubTaskById(httpExchange, rCode);
                break;
            default:
                rCode = 400;
                writeResponse(httpExchange, RequestShortRules.REQUEST_RULES_MESSAGE, rCode);
        }
    }

    /**
     * mapping methods
     */
    private void handleUpdateSubTaskById(HttpExchange httpExchange, int rCode) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

        Optional<Integer> postIdOpt = getIdFromRequestParameter(httpExchange);
        if (postIdOpt.isEmpty()) {
            writeResponse(httpExchange, "Некорректный параметр Id", 400);
            return;
        }
        int requestedId = postIdOpt.get();
        int mainTaskId = 0;

        for (Task t : serverManager.getAllTasksList().values()) {
            for (SubTask s : t.getSubTasksList()) {
                if (s.getTaskId() == requestedId) {
                    mainTaskId = t.getTaskId();
                    break;
                }
            }
        }

        if (serverManager.getTaskById(mainTaskId) != null) {
            if (body.isEmpty()) {
                response = String.valueOf(HttpFeedback.REQUEST_DOES_NOT_CONTAIN_REQUIRED_DATA);
                rCode = 400;
            } else if (!body.isEmpty()) {
                SubTask updSubTask;
                try {
                    updSubTask = gson.fromJson(body, SubTask.class);
                    serverManager.deleteSubTaskByID(requestedId);
                    updSubTask.setTaskId(requestedId);

                    serverManager.getAllTasksList()
                            .get(mainTaskId)
                            .getSubTasksList()
                            .add(updSubTask);
                    serverManager.saveData();

                    response = String.valueOf(HttpFeedback.SUB_TASK_UPDATED);
                } catch (JsonSyntaxException e) {
                    writeResponse(httpExchange, "Получен некорректный JSON", 400);
                }
            }
        } else {
            response = String.valueOf(Feedback.NON_EXISTING_TASK_ID);
            rCode = 400;
        }
        writeResponse(httpExchange, response, rCode);
    }

    private void handleUpdateTaskById(HttpExchange httpExchange, int rCode) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

        Optional<Integer> postIdOpt = getIdFromRequestParameter(httpExchange);
        if (postIdOpt.isEmpty()) {
            writeResponse(httpExchange, "Некорректный параметр Id", 400);
            return;
        }
        int requestedId = postIdOpt.get();

        if (serverManager.getTaskById(requestedId) != null) {
            if (body.isEmpty()) {
                response = String.valueOf(HttpFeedback.REQUEST_DOES_NOT_CONTAIN_REQUIRED_DATA);
                rCode = 400;
            } else if (!body.isEmpty()) {
                Task updTask;
                try {
                    updTask = gson.fromJson(body, Task.class);
                    serverManager.deleteTaskById(requestedId);
                    updTask.setTaskId(requestedId);

                    serverManager.createNewTask(updTask);
                    response = String.valueOf(HttpFeedback.TASK_UPDATED);
                } catch (JsonSyntaxException e) {
                    writeResponse(httpExchange, "Получен некорректный JSON", 400);
                }
            }
        } else {
            response = String.valueOf(Feedback.NON_EXISTING_TASK_ID);
            rCode = 400;
        }
        writeResponse(httpExchange, response, rCode);
    }

    private void handleGetAllTasks(HttpExchange httpExchange, int rCode) throws IOException {
        response = serverManager.getAllTasksList().toString();
        writeResponse(httpExchange, gson.toJson(response), rCode);
    }

    private void handleDeleteAllTasks(HttpExchange httpExchange, int rCode) throws IOException {
        serverManager.deleteAllTasks();
        writeResponse(httpExchange, String.valueOf(HttpFeedback.REQUEST_FOR_ALL_TASKS_DELETION_PROCESSED), rCode);
    }

    private void handleCreateNewTask(HttpExchange httpExchange, int rCode) throws IOException {
        int taskId = 0;
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

        if (body.isEmpty()) {
            taskId = serverManager.createNewTask().getTaskId();
        }

        if (!body.isEmpty()) {
            Task newTask;

            try {
                newTask = gson.fromJson(body, Task.class);
                serverManager.createNewTask(newTask);
                taskId = newTask.getTaskId();
            } catch (JsonSyntaxException e) {
                writeResponse(httpExchange, "Получен некорректный JSON", 400);
            }

        }
        writeResponse(httpExchange, HttpFeedback.REQUEST_FOR_NEW_TASK_CREATION_PROCESSED
                + "\n Id новой задачи = " + taskId, rCode);
    }

    private void handleGetTaskById(HttpExchange httpExchange, int rCode) throws IOException {
        Optional<Integer> postIdOpt = getIdFromRequestParameter(httpExchange);
        if (postIdOpt.isEmpty()) {
            writeResponse(httpExchange, "Некорректный параметр Id", 400);
            return;
        }

        int requestedId = postIdOpt.get();

        if (serverManager.getTaskById(requestedId) != null) {
            response = serverManager.getTaskById(requestedId).toString();
            gson.toJson(response);
        } else {
            response = String.valueOf(Feedback.NON_EXISTING_TASK_ID);
            rCode = 400;
        }
        writeResponse(httpExchange, response, rCode);
    }

    private void handleDeleteTaskById(HttpExchange httpExchange, int rCode) throws IOException {
        Optional<Integer> postIdOpt = getIdFromRequestParameter(httpExchange);
        if (postIdOpt.isEmpty()) {
            writeResponse(httpExchange, "Некорректный параметр Id", 400);
            return;
        }

        int requestedId = postIdOpt.get();
        serverManager.deleteTaskById(requestedId);
        writeResponse(httpExchange, String.valueOf(HttpFeedback.REQUEST_FOR_TASK_DELETION_PROCESSED), rCode);
    }

    private void handleGetHistory(HttpExchange httpExchange, int rCode) throws IOException {
        response = serverManager.getHistory().getHistoryList().toString();
        writeResponse(httpExchange, gson.toJson(response), rCode);
    }

    private void handleCreateSubTask(HttpExchange httpExchange, int rCode) throws IOException {
        Optional<Integer> postIdOpt = getIdFromRequestParameter(httpExchange);
        if (postIdOpt.isEmpty()) {
            writeResponse(httpExchange, "Некорректный параметр Id", 400);
            return;
        }
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        int newSubId = 0;
        int requestedId = postIdOpt.get(); //записали id

        System.out.println(serverManager.getTaskById(requestedId) == null);

        if (serverManager.getTaskById(requestedId) != null) {

            if (body.isEmpty()) {
                newSubId = serverManager.createSubTask(requestedId).getSubTaskId();

            }
            if (!body.isEmpty()) {
                SubTask newSub;

                try {
                    newSub = gson.fromJson(body, SubTask.class);
                    serverManager.createSubTask(requestedId, newSub);
                    newSubId = newSub.getTaskId();
                } catch (JsonSyntaxException e) {
                    writeResponse(httpExchange, "Получен некорректный JSON", 400);
                }
            }
            writeResponse(httpExchange, HttpFeedback.REQUEST_FOR_NEW_TASK_CREATION_PROCESSED
                    + "\n Id новой задачи = " + newSubId, rCode);

        } else {
            writeResponse(httpExchange, String.valueOf(Feedback.NON_EXISTING_TASK_ID), 400);
        }
    }

    private void handleGetSubTask(HttpExchange httpExchange, int rCode) throws IOException {
        Optional<Integer> postIdOpt = getIdFromRequestParameter(httpExchange);
        if (postIdOpt.isEmpty()) {
            writeResponse(httpExchange, "Некорректный параметр Id", 400);
            return;
        }
        int requestedId = postIdOpt.get();

        if (serverManager.getSubTaskById(requestedId) != null) {
            response = serverManager.getSubTaskById(requestedId).toString();
            gson.toJson(response);
        } else {
            response = String.valueOf(Feedback.NON_EXISTING_TASK_ID);
            rCode = 400;
        }
        writeResponse(httpExchange, response, rCode);
    }

    private void handleDeleteSubTask(HttpExchange httpExchange, int rCode) throws IOException {
        Optional<Integer> postIdOpt = getIdFromRequestParameter(httpExchange);
        if (postIdOpt.isEmpty()) {
            writeResponse(httpExchange, "Некорректный параметр Id", 400);
            return;
        }

        int requestedId = postIdOpt.get();
        Feedback f = serverManager.deleteSubTaskByID(requestedId);
        if (f.equals(Feedback.FAILED_TO_DELETE_SUBTASK_NON_EXISTING_ID)) {
            rCode = 400;
        }
        writeResponse(httpExchange, String.valueOf(f), rCode);
    }

    //Этот модель-заготовка для вызова методов менеджера по имени (для методов без параметров);
    //можно при необходимости развить для методов с параметрами, но для этого придется прописывать под каждый метод
    //обработку входящих параметров из запроса или из его тела.
    private void handleStartMethod(HttpExchange httpExchange, int rCode) throws IOException {
        Object o = "метод ничего не вернул.";

        Map<Integer, String> methodsCouldBeCalledByHttpRequest = new HashMap<>();
        methodsCouldBeCalledByHttpRequest.put(1, "organizeScheduleForAllTasks");
        methodsCouldBeCalledByHttpRequest.put(2, "deleteAllTasks");
        methodsCouldBeCalledByHttpRequest.put(3, "getDataFilePath");

        String query = String.valueOf(httpExchange.getRequestURI().getQuery());
        String[] split = query.split("=");
        String methodName = split[1];

        if (!methodsCouldBeCalledByHttpRequest.containsValue(methodName)) {
            rCode = 400;
            writeResponse(httpExchange, "Метод с именем "
                    + methodName + "не может быть выполнен через ACTION запрос.", rCode);

        } else {
            int methodToProcess = 0;

            for (Map.Entry<Integer, String> e : methodsCouldBeCalledByHttpRequest.entrySet()) {
                if (e.getValue().equalsIgnoreCase(methodName)) {
                    methodToProcess = e.getKey();
                    break;
                }
            }

            String basicReport = "Метод с именем " + methodName + " запущен. Метод вернул: ";

            switch (methodToProcess) {
                case 1:
                    o = serverManager.organizeScheduleForAllTasks();
                    writeResponse(httpExchange, basicReport + o, rCode);
                    break;
                case 2:
                    serverManager.deleteAllTasks();
                    writeResponse(httpExchange, basicReport + o, rCode);
                    break;
                case 3:
                    o = serverManager.getDataFilePath();
                    writeResponse(httpExchange, basicReport + o, rCode);
                    break;
                default:
                    writeResponse(httpExchange, "Не удалось запустить метод " + methodName + ".", rCode);
                    break;
            }
        }
    }

    /**
     * Auxiliary methods
     */

    /**
     * method for executing request parameter as int
     */
    private Optional<Integer> getIdFromRequestParameter(HttpExchange httpExchange) {
        String query = String.valueOf(httpExchange.getRequestURI().getQuery());
        String expectdAsId = "";

        try {
            String[] params = query.split("&");
            String[] split = params[0].split("=");
            expectdAsId = split[1];
        } catch (NullPointerException ex) {
        }

        try {
            return Optional.of(Integer.parseInt(expectdAsId));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    /**
     * method for picking a certain endpoint depending on incoming request path and method
     */
    private Endpoint getEndpoint(String requestPath, String requestPathFull, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        String[] pathPartsFull = requestPathFull.split("/");

        if (pathParts.length == 2 && pathPartsFull[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_ALL_TASKS;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_ALL_TASKS;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.ADD_NEW_TASK;
            }
        }

        if (pathParts.length == 3
                && pathParts[1].equals("tasks")
                && pathPartsFull[2].startsWith("task?id=")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASK_BY_ID;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_TASK_BY_ID;
            }
            if (requestMethod.equals("PATCH")) {
                return Endpoint.UPDATE_TASK_BY_ID;
            }
        }

        if (pathParts.length == 2
                && pathPartsFull[1].startsWith("tasks?methodName=")
                && requestMethod.equals("ACTION")) {
            return Endpoint.START_METHOD_BY_NAME;
        }

        if (pathParts.length == 3
                && pathParts[1].equalsIgnoreCase("tasks")
                && pathParts[2].equalsIgnoreCase("history")
                && requestMethod.equals("GET")) {  //получаем историю
            return Endpoint.GET_HISTORY;
        }

        if (pathParts.length == 3
                && pathParts[1].equalsIgnoreCase("tasks")
                && pathPartsFull[2].startsWith("subtasks?mainTaskId=")
                && requestMethod.equals("POST"))  //создаем сабтаск
        {
            return Endpoint.CREATE_NEW_SUBTASK;
        }

        if (pathParts.length == 3
                && pathParts[1].equalsIgnoreCase("tasks")
                && pathPartsFull[2].startsWith("subtasks?subId=")
                && requestMethod.equals("GET"))  //получаем сабтаск
        {
            return Endpoint.GET_SUBTASK;
        }

        if (pathParts.length == 3
                && pathParts[1].equalsIgnoreCase("tasks")
                && pathPartsFull[2].startsWith("subtasks?subId=")
                && requestMethod.equals("DELETE"))  //удаляем сабтаск
        {
            return Endpoint.DELETE_SUBTASK;
        }

        if (pathParts.length == 3
                && pathParts[1].equalsIgnoreCase("tasks")
                && pathPartsFull[2].startsWith("subtasks?subId=")
                && requestMethod.equals("PATCH"))  //обновляем сабтаск
        {
            return Endpoint.UPDATE_SUBTASK;
        }

        return Endpoint.REQUESTED_ENDPOINT_UNKNOWN;
    }

    /**
     * method for final response creation
     */
    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

}

//Спасибо за работу по ревью.