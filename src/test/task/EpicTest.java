package test.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;
import task.Epic;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {
    private static TaskManager taskManager;
    @BeforeEach
    public void BeforeEach(){
        taskManager = Managers.getDefault();
    }
    @Test
    public void shouldBeEqualWhenIdIsEqual() {
        Epic epic = new Epic("Test Name", "Test Description");
        taskManager.createEpic(epic);
        final int epicId = epic.getId();

        final Epic savedEpic = taskManager.getEpic(epicId);
        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic, savedEpic, "Эпики не совпадают");

        final ArrayList<Epic> epics = taskManager.getEpics();

        assertNotNull(epic, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков");
        assertEquals(epic, epics.getFirst(), "Эпики не равны");
    }

    @Test
    public void shouldNotAddEpicToEpicSubTasks() {
        Epic epic = new Epic("Test Name", "Test Description");
       //  assertThrows(Exception.class, () -> epic.addSubTask(epic));
        // java: incompatible types: task.Epic cannot be converted to task.SubTask
        assertNotNull(epic);
    }
}
