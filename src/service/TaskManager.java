package service;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    TreeSet<Task> getPrioritizedTasks();

    void createEpic(Epic epic);

    void createTask(task.Task task);

    void createSubTask(SubTask subTask);

    task.Task getTask(int id);

    SubTask getSubtask(int id);

    Epic getEpic(int id);

    void updateSubTask(SubTask subTask);

    void updateTask(task.Task task);

    void updateEpic(Epic epic);

    void deleteTask(int id);

    void deleteSubtask(int id);

    void deleteEpic(int id);

    ArrayList<task.Task> getTasks();

    ArrayList<SubTask> getSubtasks();

    ArrayList<Epic> getEpics();

    ArrayList<SubTask> getEpicSubtasks(Epic epic);

    void deleteAllEpics();

    void deleteAllTasks();

    void deleteAllSubTasks();

    List<Task> getHistory();
}
