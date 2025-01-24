import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;
import task.Epic;
import task.SubTask;
import task.TaskStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubTaskTest {
    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void shouldBeEqualWhenIdIsEqual() {
        Epic epic = new Epic("Test Name", "Test Description");
        SubTask subTask = new SubTask("Test Name", "Test Description", TaskStatus.NEW, epic);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        final int subTaskId = subTask.getId();

        final SubTask savedSubTask = taskManager.getSubtask(subTaskId);
        assertNotNull(savedSubTask, "Подзадача не найдена");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают");

        final ArrayList<SubTask> subTasks = taskManager.getSubtasks();

        assertNotNull(subTask, "Подзадачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач");
        assertEquals(subTask, subTasks.getFirst(), "Подзадачи не равны");
    }

}
