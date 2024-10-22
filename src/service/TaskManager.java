package service;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Epic> epicHashMap;
    private HashMap<Integer, SubTask> subTaskHashMap;
    private HashMap<Integer, Task> taskHashMap;

    public TaskManager() {
        epicHashMap = new HashMap<>();
        subTaskHashMap = new HashMap<>();
        taskHashMap = new HashMap<>();
    }

    public void createEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }

    public void createTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public void createSubTask(SubTask subTask) {
        subTaskHashMap.put(subTask.getId(), subTask);
    }

    public Task getTaskById(int id) {
        return taskHashMap.get(id);
    }

    public SubTask getSubtaskById(int id) {
        return subTaskHashMap.get(id);
    }

    public Epic getEpicById(int id) {
        return epicHashMap.get(id);
    }

    public void updateSubTask(SubTask subTask) {
        subTask.getEpic().deleteSubTask(subTask);
        subTask.getEpic().addSubTask(subTask);
        subTask.getEpic().updateStatus();
        subTaskHashMap.put(subTask.getId(), subTask);
    }

    public void updateTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }

    public void deleteTask(int id) {
        taskHashMap.remove(id);
    }

    public void deleteSubtask(int id) {
        SubTask subtask = subTaskHashMap.remove(id);
        if (subtask != null) {
            Epic epic = subtask.getEpic();
            if (epic != null) {
                epic.deleteSubTask(subtask);
                epic.updateStatus();
            }
        }
    }

    public void deleteEpic(int id) {
        Epic epic = epicHashMap.remove(id);
        if (!epic.getSubTasksList().isEmpty()) {
            for (SubTask subTask : epic.getSubTasksList()) {
                subTaskHashMap.remove(subTask.getId());
            }
            epic.getSubTasksList().clear();
        }
    }


    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskHashMap.values());
    }

    public ArrayList<SubTask> getAllSubtasks() {
        return new ArrayList<>(subTaskHashMap.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicHashMap.values());
    }

    public ArrayList<SubTask> getAllSubTasksByEpic(Epic epic) {
        return epic.getSubTasksList();
    }

    public void deleteAllEpics() {
        epicHashMap.clear();
    }

    public void deleteAllTasks() {
        taskHashMap.clear();
    }

    public void deleteAllSubTasks() {
        subTaskHashMap.clear();
    }
}
