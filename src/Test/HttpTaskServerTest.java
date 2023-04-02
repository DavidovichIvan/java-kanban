package Test;

import HTTP.KVServer;
import HTTP.KVTaskClient;
import Model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static HTTP.HttpFeedback.REQUEST_FOR_NEW_TASK_CREATION_PROCESSED;
import static HTTP.HttpFeedback.REQUEST_FOR_TASK_DELETION_PROCESSED;
import static Manager.Feedback.SUBTASK_SUCCESSFULLY_DELETED;
import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    HTTP.HttpTaskServer server = new HTTP.HttpTaskServer();
    KVServer kvServer = new KVServer();
    private final String TEST_URL = "http://localhost:8080/";
    private final String TEST_URL1 = "http://localhost:8078/";

    private final String TEST_API_TOKEN = "API_TOKEN=DEBUG";

    private String testBodytext = "{\"name\":\"testName\",\"taskDescription\":\"testDescription\"}";


    @BeforeEach
    public void beforeEach() throws IOException {
        server.startServer();
        kvServer.start();
    }

    @AfterEach
    public void afterEach() {
        server.stopServer();
        kvServer.stopServer();
    }

    @Test
    public void shouldSaveAndLoadDataOnKVServerForCertainApiToken() throws IOException, InterruptedException {

        KVTaskClient client = new KVTaskClient(TEST_API_TOKEN, TEST_URL1);

        String key = String.valueOf(6);
        String key1 = String.valueOf(7);

        String testData = "testData";
        String testData1 = "anotherTestData";

        client.put(key,testData);
        client.put(key1,testData1);

        //checking for saving data
        assertEquals(kvServer.getDataForTestsOnly().get(key),testData);
        assertEquals(kvServer.getDataForTestsOnly().get(key1),testData1);

        HttpClient client2 = HttpClient.newHttpClient();

        URI uri = URI.create(TEST_URL1 +"load/"+key+"?"+TEST_API_TOKEN);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client2.send(request, handler);

        assertEquals(response.statusCode(),200);
        assertEquals(response.body(), testData);

    }

    @Test //Endpoint.ADD_NEW_TASK
    public void shouldCreateNewTaskAndPutItIntoAlltaskslistViaRequest() throws IOException, InterruptedException {
        server.getServerManager().getAllTasksList().clear();
        URI uri = URI.create(TEST_URL + "tasks");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(testBodytext))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertAll(
                () -> assertFalse(server.getServerManager().getAllTasksList().isEmpty()),
                () -> assertTrue(response.body().contains(String.valueOf(REQUEST_FOR_NEW_TASK_CREATION_PROCESSED)))
        );
    }

    @Test //Endpoint.GET_TASK_BY_ID
    public void shouldReturnTaskById() throws IOException, InterruptedException {
        server.getServerManager().getAllTasksList().clear();
        Task testTask = new Task();
        int id = testTask.getTaskId();
        server.getServerManager().getAllTasksList().put(id, testTask);
        assertEquals(server.getServerManager()
                .getAllTasksList()
                .get(id).toString(),
                testTask.toString());

        URI uri = URI.create(TEST_URL + "tasks/task?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertTrue(response.body().contains("taskId")
                && response.body().contains("isEpic")
                && response.body().contains("name")
                && response.body().contains("taskDescription")
                && response.body().contains("taskStatus"));
    }

    @Test //Endpoint.GET_ALL_TASKS
    public void shouldReturnAllTasks() throws IOException, InterruptedException {
        server.getServerManager().getAllTasksList().clear();
        assertTrue(server.getServerManager()
                .getAllTasksList()
                .isEmpty());
        server.getServerManager().createNewTask();
        server.getServerManager().createNewTask();
        server.getServerManager().createNewTask();

        assertTrue(server.getServerManager().getAllTasksList().size() == 3);

        URI uri = URI.create(TEST_URL + "tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertAll(
                () -> assertTrue(response.statusCode() == 200),
                () -> assertTrue(response.body().contains("Task"))
        );
    }

    @Test //Endpoint.DELETE_ALL_TASKS
    public void shouldDeleteAllTasks() throws IOException, InterruptedException {
        server.getServerManager().createNewTask();
        server.getServerManager().createNewTask();

        assertTrue(!server.getServerManager().getAllTasksList().isEmpty());

        URI uri = URI.create(TEST_URL + "tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertAll(
                () -> assertTrue(server.getServerManager().getAllTasksList().isEmpty()),
                () -> assertTrue(response.statusCode() == 200)
        );
    }

    @Test //Endpoint.DELETE_TASK_BY_ID
    public void shouldDeleteTaskById() throws IOException, InterruptedException {
        server.getServerManager().getAllTasksList().clear();
        Task testTask = new Task();
        int id = testTask.getTaskId();
        server.getServerManager().getAllTasksList().put(id, testTask);

        URI uri = URI.create(TEST_URL + "tasks/task?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertAll(
                () -> assertTrue(server.getServerManager().getAllTasksList().get(id) == null),
                () -> assertTrue(response.statusCode() == 200),
                () -> assertEquals(response.body(), REQUEST_FOR_TASK_DELETION_PROCESSED.toString())
        );
    }

    @Test //Endpoint.GET_HISTORY
    public void shouldReturnHistory() throws IOException, InterruptedException {
        int id = server.getServerManager().createNewTask().getTaskId();
        server.getServerManager().getTaskById(id);
        assertFalse(server.getServerManager().getHistory().getHistoryList().isEmpty());
        assertTrue(server.getServerManager().getHistory().getHistoryList().size() == 1);

        URI uri = URI.create(TEST_URL + "tasks/history");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertAll(
                () -> assertTrue(response.body().contains("taskId")
                        && response.body().contains("isEpic")
                        && response.body().contains("name")
                        && response.body().contains("Пустая задача")),
                () -> assertTrue(response.statusCode() == 200)
        );
    }

    @Test //Endpoint.CREATE_NEW_SUBTASK
    public void shouldCreateSubTask() throws IOException, InterruptedException {
        int id = server.getServerManager().createNewTask().getTaskId();

        assertTrue(server.getServerManager()
                .getAllTasksList()
                .get(id)
                .getSubTasksList()
                .isEmpty());

        URI uri = URI.create(TEST_URL + "tasks/subtasks?mainTaskId=" + id);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(testBodytext))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertFalse(server.getServerManager()
                .getAllTasksList()
                .get(id)
                .getSubTasksList()
                .isEmpty());

        assertTrue(response.body().contains(REQUEST_FOR_NEW_TASK_CREATION_PROCESSED.toString()));
    }

    @Test //Endpoint.GET_SUBTASK && Endpoint.DELETE_SUBTASK
    public void shouldReturnSubTaskAndThenDeleteSubTask() throws IOException, InterruptedException {
        int id = server.getServerManager().createNewTask().getTaskId();
        int subId = server.getServerManager().createSubTask(id).getSubTaskId();

        URI uri = URI.create(TEST_URL + "tasks/subtasks?subId=" + subId);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertAll(
                () -> assertTrue(response.body().contains("taskId=" + subId)
                        && response.body().contains("Имя подзадачи")),
                () -> assertTrue(response.statusCode() == 200)
        );

        URI uri1 = URI.create(TEST_URL + "tasks/subtasks?subId=" + subId);

        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response1 = client1.send(request, handler1);

        assertAll(
                () -> assertEquals(response1.body(), SUBTASK_SUCCESSFULLY_DELETED.toString()),
                () -> assertTrue(response1.statusCode() == 200)
        );
    }

    @Test //Endpoint.REQUESTED_ENDPOINT_UNKNOWN
    public void shouldReturnREQUESTED_ENDPOINT_UNKNOWN() throws IOException, InterruptedException {
        URI uri = URI.create(TEST_URL + "tasks/unknown");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertTrue(response.body().contains("Поступил запрос в некорректном формате."));
    }

}
