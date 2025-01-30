import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import service.Managers;
import service.TaskManager;
import task.Task;
import task.TaskStatus;
import util.JsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestTaskHandlerTest {

    private HttpClient httpClient;
    private TaskManager taskManager;
    private HttpTaskServer server;
    private final int PORT = 8081;
    private final String baseUrl = "http://localhost:" + PORT;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = Managers.getDefault();
        server = new HttpTaskServer(PORT, taskManager);
        server.start();
        httpClient = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void testGetTasks() throws IOException, InterruptedException {

        Task task = new Task("Test Task", "Description", TaskStatus.NEW);
        taskManager.createTask(task);

        String url = baseUrl + "/tasks";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String expectedJson = JsonUtil.toJson(List.of(task));
        assertEquals(expectedJson, response.body());
    }

    @Test
    void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Description", TaskStatus.NEW);
        taskManager.createTask(task);


        String url = baseUrl + "/tasks/" + task.getId();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String expectedJson = JsonUtil.toJson(task);
        assertEquals(expectedJson, response.body());
    }

    @Test
    void testCreateTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Description", TaskStatus.NEW);
        String taskJson = JsonUtil.toJson(task);


        String url = baseUrl + "/tasks";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size());
        assertEquals(task.getName(), tasks.get(0).getName());
        assertEquals(task.getDescription(), tasks.get(0).getDescription());
        assertEquals(task.getStatus(), tasks.get(0).getStatus());
    }

    @Test
    void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Description", TaskStatus.NEW);
        taskManager.createTask(task);
        int taskId = task.getId();

        Task updatedTask = new Task("Updated Task", "Updated Description", TaskStatus.IN_PROGRESS);
        updatedTask.setId(taskId);
        String taskJson = JsonUtil.toJson(updatedTask);

        String url = baseUrl + "/tasks/" + taskId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        Task taskFromManager = taskManager.getTask(taskId);
        assertEquals(updatedTask.getName(), taskFromManager.getName());
        assertEquals(updatedTask.getDescription(), taskFromManager.getDescription());
        assertEquals(updatedTask.getStatus(), taskFromManager.getStatus());
    }

    @Test
    void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Description", TaskStatus.NEW);
        taskManager.createTask(task);
        int taskId = task.getId();

        String url = baseUrl + "/tasks/" + taskId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(0, taskManager.getTasks().size());
    }
}
