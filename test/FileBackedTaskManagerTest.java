import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTaskManager;
import task.Epic;
import task.Task;
import task.TaskStatus;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private static File tempFile = new File("temp_tasks.csv");


    @Override
    @BeforeEach
    public void setUp() {
        tempFile = new File("temp_tasks.csv");
        tempFile.deleteOnExit();
        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
    }

    @AfterEach
    public void afterEach() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubTasks();
        taskManager.deleteAllEpics();
    }

    @Test
    public void shouldLoadFromEmptyFile() {
        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Assertions.assertEquals(0, taskManager.getTasks().size());
        Assertions.assertEquals(0, taskManager.getEpics().size());
        Assertions.assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    void shouldSaveFromEmptyData() {
        taskManager.createEpic(new Epic("Test name", "test description"));
        Assertions.assertEquals(1, taskManager.getEpics().size());
        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Assertions.assertEquals(1, taskManager.getEpics().size());

        taskManager.deleteAllEpics();
        Assertions.assertEquals(0, taskManager.getEpics().size(), "Эпики должны быть удалены.");
        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Assertions.assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void shouldSaveAndLoadFromBigData() {
        for (int i = 0; i < 50; i++) {
            taskManager.createEpic(new Epic("Test name " + i, "test description"));
            taskManager.createTask(new Task("Test name " + i, "test description", TaskStatus.NEW));
        }
        Assertions.assertEquals(50, taskManager.getEpics().size());
        Assertions.assertEquals(50, taskManager.getTasks().size());

        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Assertions.assertEquals(50, taskManager.getEpics().size());
        Assertions.assertEquals(50, taskManager.getTasks().size());
    }

    @Test
    public void shouldGetById() {
        Epic epic = new Epic("Test Name", "Test Description");
        taskManager.createEpic(epic);
        final int epicId = epic.getId();

        final Epic savedEpic = taskManager.getEpic(epicId);
        assertNotNull(savedEpic, "Задача не найдена");
        assertEquals(epic, savedEpic, "Задачи не совпадают");
    }
}
