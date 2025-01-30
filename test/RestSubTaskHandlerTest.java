import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import service.Managers;
import service.TaskManager;
import task.Epic;
import task.SubTask;
import task.TaskStatus;
import util.JsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestSubTaskHandlerTest {

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
    void testGetSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test SubTask", "Description", TaskStatus.NEW, epic.getId());
        taskManager.createSubTask(subTask);

        String url = baseUrl + "/subtasks";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String expectedJson = JsonUtil.toJson(List.of(subTask));
        assertEquals(expectedJson, response.body());
    }

    @Test
    void testGetSubTaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test SubTask", "Description", TaskStatus.NEW, epic.getId());
        taskManager.createSubTask(subTask);
        int subTaskId = subTask.getId();

        String url = baseUrl + "/subtasks/" + subTaskId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String expectedJson = JsonUtil.toJson(subTask);
        assertEquals(expectedJson, response.body());
    }

    @Test
    void testCreateSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test SubTask", "Description", TaskStatus.NEW, epic.getId());
        String subTaskJson = JsonUtil.toJson(subTask);

        String url = baseUrl + "/subtasks";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<SubTask> subTasks = taskManager.getSubtasks();
        assertEquals(1, subTasks.size());
        assertEquals(subTask.getName(), subTasks.get(0).getName());
    }

    @Test
    void testUpdateSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test SubTask", "Description", TaskStatus.NEW, epic.getId());
        taskManager.createSubTask(subTask);
        int subTaskId = subTask.getId();

        SubTask updatedSubTask = new SubTask("Updated SubTask", "Updated Description", TaskStatus.IN_PROGRESS, epic.getId());
        updatedSubTask.setId(subTaskId);
        String subTaskJson = JsonUtil.toJson(updatedSubTask);

        String url = baseUrl + "/subtasks/" + subTaskId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        SubTask subTaskFromManager = taskManager.getSubtask(subTaskId);
        assertEquals(updatedSubTask.getName(), subTaskFromManager.getName());
        assertEquals(updatedSubTask.getDescription(), subTaskFromManager.getDescription());
        assertEquals(updatedSubTask.getStatus(), subTaskFromManager.getStatus());
    }

    @Test
    void testDeleteSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test SubTask", "Description", TaskStatus.NEW, epic.getId());
        taskManager.createSubTask(subTask);
        int subTaskId = subTask.getId();

        String url = baseUrl + "/subtasks/" + subTaskId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(0, taskManager.getSubtasks().size());
    }
}