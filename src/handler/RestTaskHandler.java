package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;
import task.Task;
import util.JsonUtil;

import java.io.IOException;

public class RestTaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public RestTaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        try {
            switch (endpoint) {
                case GET_TASKS: {
                    handleGetTasks(exchange);
                    break;
                }
                case GET_TASK_BY_ID: {
                    handleGetTaskById(exchange);
                    break;
                }
                case CREATE_TASK: {
                    handleCreateTask(exchange, false);
                    break;
                }
                case DELETE_TASK: {
                    handleDeleteTask(exchange);
                    break;
                }
                case UPDATE_TASK: {
                    handleCreateTask(exchange, true);
                    break;
                }
                default: {
                    sendErrorResponse(exchange, 404, "Endpoint not found");
                }
            }
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        int id = extractId(exchange.getRequestURI().getPath());
        manager.deleteTask(id);
        sendOk(exchange);
    }

    private void handleCreateTask(HttpExchange exchange, boolean update) throws IOException {
        String requestBody = readRequestBody(exchange);
        Task fromJsonTask = JsonUtil.fromJson(requestBody, Task.class);

        if (update) {
            int id = extractId(exchange.getRequestURI().getPath());
            fromJsonTask.setId(id);
            manager.updateTask(id, fromJsonTask);
        } else {
            manager.createTask(fromJsonTask);
        }
        sendOk(exchange);
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        sendJsonResponse(exchange, manager.getTasks());
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        int id = extractId(exchange.getRequestURI().getPath());
        sendJsonResponse(exchange, manager.getTask(id));
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {

        if (requestMethod.equals("GET")) {
            if (requestPath.matches("/tasks")) {
                return Endpoint.GET_TASKS;
            } else if (requestPath.matches("/tasks/\\d+")) {
                return Endpoint.GET_TASK_BY_ID;
            }
        }

        if (requestMethod.equals("POST")) {
            if (requestPath.matches("/tasks")) {
                return Endpoint.CREATE_TASK;
            } else if (requestPath.matches("/tasks/\\d+")) {
                return Endpoint.UPDATE_TASK;
            }
        }

        if (requestMethod.equals("DELETE") && requestPath.matches("/tasks/\\d+")) {
            return Endpoint.DELETE_TASK;
        }

        return Endpoint.NOT_FOUND;
    }

    enum Endpoint {GET_TASKS, GET_TASK_BY_ID, CREATE_TASK, UPDATE_TASK, DELETE_TASK, NOT_FOUND}
}