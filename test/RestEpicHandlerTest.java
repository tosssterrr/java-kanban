import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import service.Managers;
import service.TaskManager;
import task.Epic;
import util.JsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestEpicHandlerTest {

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
    void testGetEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);

        String url = baseUrl + "/epics";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String expectedJson = JsonUtil.toJson(List.of(epic));
        assertEquals(expectedJson, response.body());
    }

    @Test
    void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);
        int epicId = epic.getId();

        String url = baseUrl + "/epics/" + epicId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String expectedJson = JsonUtil.toJson(epic);
        assertEquals(expectedJson, response.body());
    }

    @Test
    void testCreateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Description");
        String epicJson = JsonUtil.toJson(epic);

        String url = baseUrl + "/epics";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size());
        assertEquals(epic.getName(), epics.get(0).getName());
    }

    @Test
    void testUpdateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);
        int epicId = epic.getId();

        Epic updatedEpic = new Epic("Updated Epic", "Updated Description");
        updatedEpic.setId(epicId);
        String epicJson = JsonUtil.toJson(updatedEpic);

        String url = baseUrl + "/epics/" + epicId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        Epic epicFromManager = taskManager.getEpic(epicId);
        assertEquals(updatedEpic.getName(), epicFromManager.getName());
        assertEquals(updatedEpic.getDescription(), epicFromManager.getDescription());
    }

    @Test
    void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);
        int epicId = epic.getId();

        String url = baseUrl + "/epics/" + epicId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(0, taskManager.getEpics().size());
    }
}