package server;

import com.sun.net.httpserver.HttpServer;
import handler.*;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final int PORT;
    private HttpServer server;
    private final TaskManager manager;

    public HttpTaskServer(int port) {
        PORT = port;
        this.manager = Managers.getFileBacked();
    }

    public HttpTaskServer(int port, TaskManager manager) {
        PORT = port;
        this.manager = manager;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new RestTaskHandler(manager));
        server.createContext("/subtasks", new RestSubTaskHandler(manager));
        server.createContext("/epics", new RestEpicHandler(manager));
        server.createContext("/history", new RestHistoryHandler(manager));
        server.createContext("/prioritized", new RestPrioritizedHandler(manager));
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("HTTP-сервер остановлен.");
        }
    }

}
