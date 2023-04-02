package HTTP;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_CHARSET;

public class KVServer {
    public static final int PORT = 8078;
    private final String apiToken;
    private final HttpServer server;

    public Map<String, String> getDataForTestsOnly() {
        return data;
    }

    private final Map<String, String> data = new HashMap<>();

    public KVServer() {
        apiToken = generateApiToken();
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);
    }

    private void load(HttpExchange h) throws IOException {
        System.out.println("\n/load");
        try {
            if (!hasAuth(h)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if (h.getRequestMethod().equals("GET")) {

                String key = h.getRequestURI().getPath().substring("/load/".length());

                if (key.isEmpty()) {
                    System.out.println("Key для загрузки данных пустой. key указывается в пути: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }

                if (!data.containsKey(key)) {
                    System.out.println("Переданный ключ отсутствует в хранилище данных.");
                    h.sendResponseHeaders(400, 0);
                    return;
                }

                if (data.get(key).isEmpty()) {
                    System.out.println("По запрошенному ключу информация не записана.");
                    h.sendResponseHeaders(400, 0);
                    return;
                }

                String value = data.get(key);

                byte[] bytes = value.getBytes(DEFAULT_CHARSET);
                h.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = h.getResponseBody()) {
                    os.write(bytes);
                    System.out.println("Значение для ключа " + key + " успешно возвращено!");
                }
            } else {
                System.out.println("/load ждёт GET-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void save(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/save");

            if (!hasAuth(h)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }

            if (h.getRequestMethod().equals("POST")) {

                String key = h.getRequestURI().getPath().substring("/save/".length());

                if (key.isEmpty()) {
                    System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                String value = readText(h);

                System.out.println("Получено тело запроса - " + value);

                if (value.isEmpty()) {
                    System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                data.put(key, value);
                System.out.println("Значение для ключа " + key + " успешно обновлено!");
                h.sendResponseHeaders(200, 0);
            } else {
                System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void register(HttpExchange h) throws IOException {

        try {
            System.out.println("\n/register");
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, apiToken);
            } else {
                System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    public void start() {
      //System.out.println("API_TOKEN: " + apiToken);
        server.start();

    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.equals("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    public void stopServer() {
        server.stop(0);
    }

}
