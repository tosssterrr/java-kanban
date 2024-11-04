package service;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class InMemoryTaskManager extends InMemoryHistoryManager implements TaskManager {
    private final HashMap<Integer, Epic> epicHashMap;
    private final HashMap<Integer, SubTask> subTaskHashMap;
    private final HashMap<Integer, Task> taskHashMap;

    public InMemoryTaskManager() {
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
        this.add(task);
        return task;

    }

    @Override
    public SubTask getSubtask(int id) {
        SubTask subTask = subTaskHashMap.get(id);
        this.add(subTask);
        return subTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epicHashMap.get(id);
        this.add(epic);
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
        historyList.remove(taskHashMap.remove(id));
    }

    @Override
    public void deleteSubtask(int id) {
        SubTask subtask = subTaskHashMap.remove(id);
        historyList.remove(subtask);
        if (subtask != null) {
            Epic epic = subtask.getEpic();
            if (epic != null) {
                epic.deleteSubTask(subtask);
                epic.updateStatus();
            }
        }
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epicHashMap.remove(id);
        historyList.remove(epic);
        if (!epic.getSubTasksList().isEmpty()) {
            for (SubTask subTask : epic.getSubTasksList()) {
                subTaskHashMap.remove(subTask.getId());
                historyList.remove(subTask);
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
        deleteFromHistory(subTaskHashMap.values());
        deleteFromHistory(epicHashMap.values());
        epicHashMap.clear();
        subTaskHashMap.clear();
    }

    @Override
    public void deleteAllTasks() {
        deleteFromHistory(taskHashMap.values());
        taskHashMap.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (SubTask subTask : subTaskHashMap.values()) {
            Epic epic = subTask.getEpic();
            epic.deleteSubTask(subTask);
            historyList.remove(subTask);
        }
        subTaskHashMap.clear();
    }
    private void deleteFromHistory(Collection<? extends Task> tasks) {
        for (Task task : tasks) {
            historyList.remove(task);
        }
    }
}
