package HTTP;

import Interfaces.TaskManager;
import Manager.Managers;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    private final String DEFAULT_HTTP_DATA_PATH = "C:\\Users\\Вуня\\Desktop\\dev\\6th sprint\\java-kanban\\src\\DataStorage";
    protected TaskManager serverManager;
    HttpServer httpServer;
    public HttpTaskServer() {
        this.serverManager = Managers.getManagerWithBackup(DEFAULT_HTTP_DATA_PATH);
    }


    public void startServer() throws IOException {
        System.setProperty("jdk.httpclient.keepalive.timeout", "99999");
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(serverManager));

        httpServer.start();

}
    public void stopServer() {
        httpServer.stop(0);
      }

    public TaskManager getServerManager() {
        return serverManager;
    }

    public void setServerManager(TaskManager serverManager) {
        this.serverManager = serverManager;
    }

    public String getDEFAULT_HTTP_DATA_PATH() {
        return DEFAULT_HTTP_DATA_PATH;
    }

}


