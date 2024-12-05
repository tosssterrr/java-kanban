import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;
import task.Task;
import task.TaskStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void shouldBeEqualWhenIdIsEqual() {
        Task task = new Task("Test Name", "Test Description", TaskStatus.NEW);
        taskManager.createTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTask(taskId);
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");

        final ArrayList<Task> tasks = taskManager.getTasks();

        assertNotNull(task, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(task, tasks.getFirst(), "Задачи не равны");
    }




}