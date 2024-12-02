package test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import service.HistoryManager;
import service.InMemoryHistoryManager;
import service.Managers;
import service.TaskManager;
import task.Epic;
import task.Task;
import task.TaskStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldAddDifferentTypesOfTasks() {
        Task task = new Task("Test Name", "Test Description", TaskStatus.NEW);
        Epic epic = new Epic("Test Name", "Test Description");

        historyManager.add(task);
        historyManager.add(epic);

        assertTrue(historyManager.getHistory().contains(task));
        assertTrue(historyManager.getHistory().contains(epic));
    }

    @Test
    public void shouldNotAddToHistoryIfNull() {
        TaskManager taskManager = Managers.getDefault();
        taskManager.getTask(0);
        historyManager.add(null);
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    public void shouldNotChangeFieldsWhenAddedToHistory() {
        Task task = new Task("Test Name", "Test Description", TaskStatus.NEW);
        historyManager.add(task);

        List<Task> historyList = historyManager.getHistory();
        assertEquals(1, historyList.size());

        Task taskFromHistory = historyList.getFirst();
        assertEquals(task.getName(), taskFromHistory.getName());
        assertEquals(task.getDescription(), taskFromHistory.getDescription());
        assertEquals(task.getStatus(), taskFromHistory.getStatus());
        assertEquals(task.getId(), taskFromHistory.getId());
    }

    @Test
    public void shouldAddToEndOfListIfAlreadyExist() {
        Task task = new Task("Task 1", "Test Description", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Test Description", TaskStatus.NEW);
        Task task3 = new Task("Task 3", "Test Description", TaskStatus.NEW);
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task2);
        List<Task> checkTasks = new ArrayList<>(List.of(task, task3, task2));
        assertEquals(checkTasks, historyManager.getHistory());
    }
}
