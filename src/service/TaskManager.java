package service;

import task.Epic;
import task.SubTask;

import java.util.ArrayList;

public interface TaskManager  {
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
}
