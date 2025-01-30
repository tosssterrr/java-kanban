package server;

import com.sun.net.httpserver.HttpServer;
import handler.*;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final int port;
    private HttpServer server;
    private final TaskManager manager;

    public HttpTaskServer(int port) {
        this.port = port;
        this.manager = Managers.getFileBacked();
    }

    public HttpTaskServer(int port, TaskManager manager) {
        this.port = port;
        this.manager = manager;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/tasks", new RestTaskHandler(manager));
        server.createContext("/subtasks", new RestSubTaskHandler(manager));
        server.createContext("/epics", new RestEpicHandler(manager));
        server.createContext("/history", new RestHistoryHandler(manager));
        server.createContext("/prioritized", new RestPrioritizedHandler(manager));
        server.start();
        System.out.println("HTTP-сервер запущен на " + port + " порту!");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("HTTP-сервер остановлен.");
        }
    }

}
