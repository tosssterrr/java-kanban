package service;

import service.exceptions.TaskTimeOverlapException;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Epic> epicHashMap;
    protected final Map<Integer, SubTask> subTaskHashMap;
    protected final Map<Integer, Task> taskHashMap;
    protected final HistoryManager historyManager;
    protected final Set<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
        epicHashMap = new HashMap<>();
        subTaskHashMap = new HashMap<>();
        taskHashMap = new HashMap<>();
        prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public void createEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }

    @Override
    public void createTask(Task task) {
        if (!validateTime(task)) {
            throw new TaskTimeOverlapException("Время выполнения задачи пересекаются с существующими задачами.");
        }
        taskHashMap.put(task.getId(), task);
        addPrioritizedTask(task);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        if (!validateTime(subTask)) {
            throw new TaskTimeOverlapException("Время выполнения задачи пересекаются с существующими задачами.");
        }
        epicHashMap.get(subTask.getEpicId()).addSubTask(subTask);
        epicHashMap.get(subTask.getEpicId()).updateStatus();
        subTaskHashMap.put(subTask.getId(), subTask);
        addPrioritizedTask(subTask);
    }

    private void addPrioritizedTask(Task task) {
        Optional.ofNullable(task.getStartTime()).ifPresent(startTime -> prioritizedTasks.add(task));
    }

    private boolean validateTime(Task newTask) {
        return prioritizedTasks.stream().noneMatch(existing -> isOverlapping(newTask, existing));
    }

    public boolean isOverlapping(Task newTask, Task exictingTask) {
        return newTask.getStartTime().isBefore(exictingTask.getEndTime()) &&
                newTask.getEndTime().isAfter(exictingTask.getStartTime());
    }

    @Override
    public Task getTask(int id) {
        Task task = taskHashMap.get(id);
        if (task == null) {
            throw new NoSuchElementException();
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public SubTask getSubtask(int id) {
        SubTask subTask = subTaskHashMap.get(id);
        if (subTask == null) {
            throw new NoSuchElementException();
        }
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epicHashMap.get(id);
        if (epic == null) {
            throw new NoSuchElementException();
        }
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void updateSubTask(int id, SubTask subTask) {
        if (!validateTime(subTask)) {
            throw new TaskTimeOverlapException("Время выполнения задачи пересекаются с существующими задачами.");
        }
        Epic epic = this.epicHashMap.get(subTask.getEpicId());
        epic.deleteSubTask(subTask);
        epic.addSubTask(subTask);
        epic.updateStatus();
        subTaskHashMap.put(id, subTask);
        prioritizedTasks.remove(subTask);
        addPrioritizedTask(subTask);
    }

    @Override
    public void updateTask(int id, Task task) {
        if (!validateTime(task)) {
            throw new TaskTimeOverlapException("Время выполнения задачи пересекаются с существующими задачами.");
        }
        taskHashMap.put(id, task);
        prioritizedTasks.remove(task);
        addPrioritizedTask(task);
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        epicHashMap.put(id, epic);
    }

    @Override
    public void deleteTask(int id) {
        prioritizedTasks.remove(taskHashMap.remove(id));
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        SubTask subTask = subTaskHashMap.remove(id);
        if (subTask != null) {
            Epic epic = this.getEpic(subTask.getEpicId());
            if (epic != null) {
                epic.deleteSubTask(subTask);
                epic.updateStatus();
                historyManager.remove(id);
                prioritizedTasks.remove(subTask);
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
    public ArrayList<SubTask> getEpicSubtasks(int id) {
        return epicHashMap.get(id).getSubTasksList();
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epicHashMap.values()) {
            historyManager.remove(epic.getId());
        }
        for (SubTask subTask : subTaskHashMap.values()) {
            historyManager.remove(subTask.getId());
        }
        epicHashMap.clear();
        subTaskHashMap.clear();
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : taskHashMap.values()) {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        }
        taskHashMap.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (SubTask subTask : subTaskHashMap.values()) {
            Epic epic = this.getEpic(subTask.getEpicId());
            epic.deleteSubTask(subTask);
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTask);
        }
        subTaskHashMap.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
