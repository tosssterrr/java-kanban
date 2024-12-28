import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import task.Epic;
import task.Task;
import task.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override @BeforeEach
    public void setUp() {
        this.taskManager = new InMemoryTaskManager();
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

    @Test
    public void shouldNotChangeFieldsWhenAddedToManager() {
        Task originalTask = new Task("Task 1", "Test Description", TaskStatus.NEW);
        String originalName = originalTask.getName();
        String originalDescription = originalTask.getDescription();
        TaskStatus originalStatus = originalTask.getStatus();

        taskManager.createTask(originalTask);

        Task retrievedTask = taskManager.getTask(originalTask.getId());

        assertEquals(originalName, retrievedTask.getName());
        assertEquals(originalDescription, retrievedTask.getDescription());
        assertEquals(originalStatus, retrievedTask.getStatus());
    }
}
