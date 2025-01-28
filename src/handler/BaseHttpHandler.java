package handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import service.exceptions.ManagerSaveException;
import service.exceptions.TaskTimeOverlapException;
import util.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

public class BaseHttpHandler {

    protected String readRequestBody(HttpExchange exchange) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        return requestBody.toString();
    }

    protected void sendErrorResponse(HttpExchange exchange, int statusCode, String errorMessage) throws IOException {
        String response = String.format("{\"status\": \"error\", \"message\": \"%s\"}", errorMessage);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.length());
        exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
        exchange.getResponseBody().close();
    }

    protected void handleException(HttpExchange exchange, Exception e) throws IOException {
        if (e instanceof NoSuchElementException | e instanceof NullPointerException) {
            sendErrorResponse(exchange, 404, "Resource not found");
        } else if (e instanceof NumberFormatException) {
            sendErrorResponse(exchange, 400, "Неправильный запрос");
        } else if (e instanceof TaskTimeOverlapException) {
            sendErrorResponse(exchange, 406, "У задачи есть пересечения по времени");
        } else if (e instanceof ManagerSaveException) {
            sendErrorResponse(exchange, 500, "Ошибка при сохранении данных");
        } else if (e instanceof JsonSyntaxException) {
            sendErrorResponse(exchange, 400, "Неверный JSON формат");
        } else {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    protected int extractId(String path) {
        String[] parts = path.split("/");
        return Integer.parseInt(parts[2]); // parts[2] — это ID
    }

    protected void sendOk(HttpExchange h) throws IOException {
        h.sendResponseHeaders(201, 0);
        h.close();
    }

    protected void sendJsonResponse(HttpExchange exchange, Object data) throws IOException {
        String jsonResponse = JsonUtil.toJson(data);
        byte[] resp = jsonResponse.getBytes();
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }
}
