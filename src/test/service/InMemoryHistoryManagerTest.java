package test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import service.HistoryManager;
import service.InMemoryHistoryManager;
import task.Epic;
import task.Task;
import task.TaskStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    public void BeforeEach() {
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
    public void shouldNotChangeFieldsWhenAddedToHistory() {
        Task task = new Task("Test Name", "Test Description", TaskStatus.NEW);
        historyManager.add(task);

        ArrayList<Task> historyList = historyManager.getHistory();
        assertEquals(1, historyList.size());

        Task taskFromHistory = historyList.getFirst();
        assertEquals(task.getName(), taskFromHistory.getName());
        assertEquals(task.getDescription(), taskFromHistory.getDescription());
        assertEquals(task.getStatus(), taskFromHistory.getStatus());
        assertEquals(task.getId(), taskFromHistory.getId());
    }
    @Test
    void testAddingMoreThanTenTasks() {
        for (int i = 0; i < 12; i++) {
            Task task = new Task("Задача " + i, "Описание задачи " + i, TaskStatus.NEW);
            historyManager.add(task);
        }

        ArrayList<Task> tasksInHistory = historyManager.getHistory();

        // Проверяем размер истории - должно быть только последние 10 задач.
        assertEquals(10, tasksInHistory.size(), "История должна содержать только последние 10 задач.");

        for (int i = 2; i < 12; i++) {
            String expectedName = "Задача " + i;
            String expectedDescription = "Описание задачи " + i;
            assertEquals(expectedName, tasksInHistory.get(i - 2).getName(), "Имя задачи не совпадает.");
            assertEquals(expectedDescription, tasksInHistory.get(i - 2).getDescription(), "Описание задачи не совпадает.");
        }
    }
}
