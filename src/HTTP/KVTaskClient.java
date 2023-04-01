package HTTP;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String serverURL;

    private final String apiToken;

    public String getApiToken() {
        return apiToken;
    }

    /**
     * constructor for already registered users who already have an apiToken
     */
    public KVTaskClient(String apiToken, String serverURL) {
        this.serverURL = serverURL;
        this.apiToken = apiToken;
    }

    /**
     * constructor for new users who don't have an apiToken
     */
    public KVTaskClient(String serverURL) throws IOException, InterruptedException {
        this.serverURL = serverURL;

        URI uri = URI.create(serverURL + "register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());

        this.apiToken = response.body();

    }

    /**
     * request for uploading data onto server
     */

    public void put(String key, String json) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(serverURL + "save/" + key + "?" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
    }

    /**
     * request for loading data from server
     */
    public void load(String key) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        URI uri = URI.create(serverURL + "load/" + key + "?" + apiToken);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);


    }
}
