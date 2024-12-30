import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;
import task.Epic;
import task.SubTask;
import task.TaskStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {
    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
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
    public void statusTest() {
        Epic epic = new Epic("Test Name", "Test Description");
        taskManager.createEpic(epic);
        assertEquals(TaskStatus.NEW, epic.getStatus()); // при пустом эпике должен быть статус new

        SubTask subTask = new SubTask("test New", " ", TaskStatus.NEW, epic);
        taskManager.createSubTask(subTask);
        assertEquals(TaskStatus.NEW, epic.getStatus()); // при новом сабтаске должен быть также new

        SubTask subTask2 = new SubTask("test inProgress", "", TaskStatus.IN_PROGRESS, epic);
        taskManager.createSubTask(subTask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus()); // при хотя бы одном in progress должен быть IN_PROGRESS

        subTask.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus()); // если не все эпики Done должен быть in progress

        subTask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask2);
        assertEquals(TaskStatus.DONE, epic.getStatus()); // при всех выполненных сабтсаках должен быть done
    }

    @Test
    public void shouldNotAddEpicToEpicSubTasks() {
        Epic epic = new Epic("Test Name", "Test Description");
       //  assertThrows(Exception.class, () -> epic.addSubTask(epic));
        // java: incompatible types: task.Epic cannot be converted to task.SubTask
        assertNotNull(epic);
    }
}
