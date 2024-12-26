import org.junit.jupiter.api.*;
import service.FileBackedTaskManager;
import task.Epic;
import task.Task;
import task.TaskStatus;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    private static FileBackedTaskManager fileTaskManager;
    private static File tempFile;

    @BeforeAll
    public static void beforeAll() {
        tempFile = new File("temp_tasks.csv");
        tempFile.deleteOnExit();
        fileTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
    }

    @AfterEach
    public void afterEach() {
        fileTaskManager.deleteAllTasks();
        fileTaskManager.deleteAllSubTasks();
        fileTaskManager.deleteAllEpics();
    }

    @Test
    public void shouldLoadFromEmptyFile() {
        fileTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Assertions.assertEquals(0, fileTaskManager.getTasks().size());
        Assertions.assertEquals(0, fileTaskManager.getEpics().size());
        Assertions.assertEquals(0, fileTaskManager.getSubtasks().size());
    }

    @Test
    void shouldSaveFromEmptyData() {
        fileTaskManager.createEpic(new Epic("Test name", "test description"));
        Assertions.assertEquals(1, fileTaskManager.getEpics().size());
        fileTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Assertions.assertEquals(1, fileTaskManager.getEpics().size());

        fileTaskManager.deleteAllEpics();
        Assertions.assertEquals(0, fileTaskManager.getEpics().size(), "Эпики должны быть удалены.");
        fileTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Assertions.assertEquals(0, fileTaskManager.getEpics().size());
    }

    @Test
    void shouldSaveAndLoadFromBigData() {
        for (int i = 0; i < 50; i++) {
            fileTaskManager.createEpic(new Epic("Test name " + i, "test description"));
            fileTaskManager.createTask(new Task("Test name " + i, "test description", TaskStatus.NEW));
        }
        Assertions.assertEquals(50, fileTaskManager.getEpics().size());
        Assertions.assertEquals(50, fileTaskManager.getTasks().size());

        fileTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Assertions.assertEquals(50, fileTaskManager.getEpics().size());
        Assertions.assertEquals(50, fileTaskManager.getTasks().size());
    }

    @Test
    public void shouldGetById() {
        Epic epic = new Epic("Test Name", "Test Description");
        fileTaskManager.createEpic(epic);
        final int epicId = epic.getId();

        final Epic savedEpic = fileTaskManager.getEpic(epicId);
        assertNotNull(savedEpic, "Задача не найдена");
        assertEquals(epic, savedEpic, "Задачи не совпадают");
    }
}
