package task;

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
        subTask.getEpic().updateStatus();
        subTaskHashMap.put(subTask.getId(), subTask);
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
            Epic epic = epicHashMap.get(subtask.getId());
            if (epic != null) {
                epic.deleteSubTask(subtask);
                epic.updateStatus();
            }
        }
    }

    public void deleteEpic(int id, boolean isSave) {
        Epic epic = epicHashMap.get(id);
        if (isSave) {
            if (!epic.getSubTasksList().isEmpty()) {
                System.out.println("Сначала удалите подзадачи этого эпика или воспользуйтесь не безопасным удалением.");
                return;
            }
        } else {
            if (!epic.getSubTasksList().isEmpty()) {
                for (SubTask subTask : epic.getSubTasksList()) {
                    deleteSubtask(subTask.getId());
                }
            }
        }
        epicHashMap.remove(id);

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
}
