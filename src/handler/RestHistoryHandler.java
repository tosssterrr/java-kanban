package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;

public class RestHistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public RestHistoryHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            sendJsonResponse(exchange, manager.getHistory());
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }
}
