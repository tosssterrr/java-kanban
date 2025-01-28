package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;
import task.Epic;
import util.JsonUtil;

import java.io.IOException;

public class RestEpicHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager manager;

    public RestEpicHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        try {
            switch (endpoint) {
                case GET_EPICS: {
                    handleGetEpics(exchange);
                    break;
                }
                case GET_EPIC_BY_ID: {
                    handleGetEpicById(exchange);
                    break;
                }
                case CREATE_EPIC: {
                    handleCreateEpic(exchange, false);
                    break;
                }
                case DELETE_EPIC: {
                    handleDeleteEpic(exchange);
                    break;
                }
                case UPDATE_EPIC: {
                    handleCreateEpic(exchange, true);
                    break;
                }
                case GET_EPICS_SUBTASKS: {
                    handleGetEpicSubTasks(exchange);
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

    private void handleGetEpicSubTasks(HttpExchange exchange) throws IOException {
        int id = extractId(exchange.getRequestURI().getPath());
        sendJsonResponse(exchange, manager.getEpicSubtasks(id));
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        int id = extractId(exchange.getRequestURI().getPath());
        sendJsonResponse(exchange, manager.getEpic(id));
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        int id = extractId(exchange.getRequestURI().getPath());
        manager.deleteEpic(id);
        sendOk(exchange);
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        sendJsonResponse(exchange, manager.getEpics());
    }

    private void handleCreateEpic(HttpExchange exchange, boolean update) throws IOException {
        String requestBody = readRequestBody(exchange);
        Epic fromJsonEpic = JsonUtil.fromJson(requestBody, Epic.class);
        if (update) {
            int id = extractId(exchange.getRequestURI().getPath());
            fromJsonEpic.setId(id);
            manager.getEpicSubtasks(id).forEach(fromJsonEpic::addSubTask);
            manager.updateEpic(id, fromJsonEpic);
        } else {
            manager.createEpic(fromJsonEpic);
        }
        sendOk(exchange);
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {

        if (requestMethod.equals("GET")) {
            if (requestPath.matches("/epics")) {
                return Endpoint.GET_EPICS;
            } else if (requestPath.matches("/epics/\\d+")) {
                return Endpoint.GET_EPIC_BY_ID;
            } else if (requestPath.matches("/epics/\\d+/subtasks")) {
                return Endpoint.GET_EPICS_SUBTASKS;
            }
        }

        if (requestMethod.equals("POST")) {
            if (requestPath.matches("/epics")) {
                return Endpoint.CREATE_EPIC;
            } else if (requestPath.matches("/epics/\\d+")) {
                return Endpoint.UPDATE_EPIC;
            }
        }

        if (requestMethod.equals("DELETE") && requestPath.matches("/epics/\\d+")) {
            return Endpoint.DELETE_EPIC;
        }

        return Endpoint.NOT_FOUND;
    }

    enum Endpoint {GET_EPICS, GET_EPIC_BY_ID, CREATE_EPIC, DELETE_EPIC, UPDATE_EPIC, NOT_FOUND, GET_EPICS_SUBTASKS}
}
