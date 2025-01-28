package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;
import task.SubTask;
import util.JsonUtil;

import java.io.IOException;

public class RestSubTaskHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager manager;

    public RestSubTaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        try {
            switch (endpoint) {
                case GET_SUBTASKS: {
                    handleGetSubTasks(exchange);
                    break;
                }
                case GET_SUBTASK_BY_ID: {
                    handleGetSubTaskById(exchange);
                    break;
                }
                case DELETE_SUBTASK: {
                    handleDeleteSubTask(exchange);
                    break;
                }
                case CREATE_SUBTASK: {
                    handleCreateSubTask(exchange, false);
                    break;
                }
                case UPDATE_SUBTASK: {
                    handleCreateSubTask(exchange, true);
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

    private void handleCreateSubTask(HttpExchange exchange, boolean update) throws IOException {
        String requestBody = readRequestBody(exchange);
        SubTask fromJsonSubTask = JsonUtil.fromJson(requestBody, SubTask.class);

        if (update) {
            int id = extractId(exchange.getRequestURI().getPath());
            fromJsonSubTask.setId(id);
            manager.updateSubTask(id, fromJsonSubTask);
            sendOk(exchange);
        } else {
            manager.createSubTask(fromJsonSubTask);
        }
        sendOk(exchange);
    }

    private void handleDeleteSubTask(HttpExchange exchange) throws IOException {
        int id = extractId(exchange.getRequestURI().getPath());
        manager.deleteSubtask(id);
        sendOk(exchange);
    }

    private void handleGetSubTaskById(HttpExchange exchange) throws IOException {
        int id = extractId(exchange.getRequestURI().getPath());
        sendJsonResponse(exchange, manager.getSubtask(id));

    }

    private void handleGetSubTasks(HttpExchange exchange) throws IOException {
        sendJsonResponse(exchange, manager.getSubtasks());
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {

        if (requestMethod.equals("GET")) {
            if (requestPath.matches("/subtasks")) {
                return Endpoint.GET_SUBTASKS;
            } else if (requestPath.matches("/subtasks/\\d+")) {
                return Endpoint.GET_SUBTASK_BY_ID;
            }
        }

        if (requestMethod.equals("POST")) {
            if (requestPath.matches("/subtasks")) {
                return Endpoint.CREATE_SUBTASK;
            } else if (requestPath.matches("/subtasks/\\d+")) {
                return Endpoint.UPDATE_SUBTASK;
            }
        }

        if (requestMethod.equals("DELETE") && requestPath.matches("/subtasks/\\d+")) {
            return Endpoint.DELETE_SUBTASK;
        }

        return Endpoint.NOT_FOUND;
    }

    enum Endpoint {GET_SUBTASKS, GET_SUBTASK_BY_ID, CREATE_SUBTASK, DELETE_SUBTASK, UPDATE_SUBTASK, NOT_FOUND}
}
