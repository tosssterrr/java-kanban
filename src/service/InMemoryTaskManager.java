package service;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Epic> epicHashMap;
    private final HashMap<Integer, SubTask> subTaskHashMap;
    private final HashMap<Integer, Task> taskHashMap;
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
        epicHashMap = new HashMap<>();
        subTaskHashMap = new HashMap<>();
        taskHashMap = new HashMap<>();
    }

    @Override
    public void createEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }

    @Override
    public void createTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        subTaskHashMap.put(subTask.getId(), subTask);
    }

    @Override
    public Task getTask(int id) {
        Task task = taskHashMap.get(id);
        historyManager.add(task);
        return task;

    }

    @Override
    public SubTask getSubtask(int id) {
        SubTask subTask = subTaskHashMap.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epicHashMap.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTask.getEpic().deleteSubTask(subTask);
        subTask.getEpic().addSubTask(subTask);
        subTask.getEpic().updateStatus();
        subTaskHashMap.put(subTask.getId(), subTask);
    }

    @Override
    public void updateTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }

    @Override
    public void deleteTask(int id) {
        taskHashMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        SubTask subTask = subTaskHashMap.remove(id);
        if (subTask != null) {
            Epic epic = subTask.getEpic();
            if (epic != null) {
                epic.deleteSubTask(subTask);
                epic.updateStatus();
                historyManager.remove(id);
            }
        }
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epicHashMap.remove(id);
        historyManager.remove(id);
        if (!epic.getSubTasksList().isEmpty()) {
            for (SubTask subTask : epic.getSubTasksList()) {
                historyManager.remove(subTask.getId());
                subTaskHashMap.remove(subTask.getId());
            }
            epic.getSubTasksList().clear();
        }
    }


    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(taskHashMap.values());
    }

    @Override
    public ArrayList<SubTask> getSubtasks() {
        return new ArrayList<>(subTaskHashMap.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epicHashMap.values());
    }

    @Override
    public ArrayList<SubTask> getEpicSubtasks(Epic epic) {
        return epic.getSubTasksList();
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epicHashMap.values()) {
            historyManager.remove(epic.getId());
        }
        for (SubTask subTask: subTaskHashMap.values()) {
            historyManager.remove(subTask.getId());
        }
        epicHashMap.clear();
        subTaskHashMap.clear();
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : taskHashMap.values()) {
            historyManager.remove(task.getId());
        }
        taskHashMap.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (SubTask subTask : subTaskHashMap.values()) {
            Epic epic = subTask.getEpic();
            epic.deleteSubTask(subTask);
            historyManager.remove(subTask.getId());
        }
        subTaskHashMap.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
