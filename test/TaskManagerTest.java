import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaskManager;
import service.exceptions.TaskTimeOverlapException;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @BeforeEach
    public abstract void setUp();

    @Test
    public void testCreateTask() {
        Task task = new Task("Test task", "Description", TaskStatus.NEW);
        taskManager.createTask(task);

        assertEquals(1, taskManager.getTasks().size());
        assertEquals(task, taskManager.getTasks().get(0));
    }

    @Test
    public void testCreateEpic() {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);

        assertEquals(1, taskManager.getEpics().size());
        assertEquals(epic, taskManager.getEpics().get(0));
    }

    @Test
    public void testCreateSubTask() {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test Subtask", "Description", TaskStatus.NEW, epic);
        taskManager.createSubTask(subTask);

        assertEquals(1, taskManager.getSubtasks().size());
        assertEquals(subTask, taskManager.getSubtasks().get(0));
    }

    @Test
    public void shouldNotCreateOverlappingDate() {
        LocalDateTime taskTime = LocalDateTime.of(2020, 1, 1, 0, 0);
        Task task = new Task("test task", "desc", TaskStatus.NEW,
                taskTime, Duration.ofMinutes(20));
        Task task2 = new Task("overlapping task", "desc", TaskStatus.NEW,
                taskTime.minusMinutes(10), Duration.ofMinutes(20));
        taskManager.createTask(task);
        assertThrows(TaskTimeOverlapException.class, () -> taskManager.createTask(task2));
        assertNull(taskManager.getTask(task2.getId()));
    }

    @Test
    public void testGetTaskById() {
        Task task = new Task("Test task", "Description", TaskStatus.NEW);
        taskManager.createTask(task);

        assertEquals(task, taskManager.getTask(task.getId()));
    }

    @Test
    public void testGetEpicById() {
        Epic epic = new Epic("Test Epic", "Description");
        final int epicId = epic.getId();
        taskManager.createEpic(epic);

        assertEquals(epic, taskManager.getEpic(epicId));
    }

    @Test
    public void testGetSubTaskById() {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test Subtask", "Description", TaskStatus.NEW, epic);
        final int subtaskId = subTask.getId();
        taskManager.createSubTask(subTask);

        assertEquals(subTask, taskManager.getSubtask(subtaskId));
    }

    @Test
    public void testUpdateTask() {
        Task original = new Task("Original", "Description", TaskStatus.NEW);
        taskManager.createTask(original);

        original.setName("Updated");
        taskManager.updateTask(original);

        assertEquals("Updated", taskManager.getTask(original.getId()).getName());
    }

    @Test
    public void testUpdateSubTask() {
        SubTask original = new SubTask("Original", "Description", TaskStatus.NEW,
                new Epic("test", "test"));
        taskManager.createSubTask(original);

        original.setName("Updated");
        taskManager.updateSubTask(original);

        assertEquals("Updated", taskManager.getSubtask(original.getId()).getName());
    }

    @Test
    public void testUpdateEpic() {
        Epic original = new Epic("test", "test");
        taskManager.createEpic(original);

        original.setName("Updated");
        taskManager.updateEpic(original);

        assertEquals("Updated", taskManager.getEpic(original.getId()).getName());
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task("To be deleted", "Description", TaskStatus.NEW);
        taskManager.createTask(task);
        taskManager.deleteTask(task.getId());
        assertEquals(0, taskManager.getTasks().size());

        assertNull(taskManager.getTask(task.getId()));
    }

    @Test
    public void testDeleteEpic() {
        Epic epic = new Epic("To be deleted", "Description");
        taskManager.createEpic(epic);
        taskManager.deleteEpic(epic.getId());

        assertEquals(0, taskManager.getEpics().size());
        assertNull(taskManager.getTask(epic.getId()));
    }

    @Test
    public void testDeleteSubTask() {
        SubTask subTask = new SubTask("Original", "Description", TaskStatus.NEW,
                new Epic("test", "test"));
        taskManager.createSubTask(subTask);
        taskManager.deleteSubtask(subTask.getId());

        assertEquals(0, taskManager.getSubtasks().size());
        assertNull(taskManager.getTask(subTask.getId()));
    }

    @Test
    public void testGetEpicSubtasks() {
        Epic epic = new Epic("test", "test");
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SubTask subTask = new SubTask("test " + i, "desc", TaskStatus.NEW, epic);
            subTasks.add(subTask);
        }
        assertEquals(subTasks, epic.getSubTasksList());
    }
}