package service;

import service.exceptions.ManagerSaveException;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final String FILE_NAME = "tasks.csv";

    private void save() {
        File file = new File(FILE_NAME);

        try {
            if (!file.exists()){
                file.createNewFile();
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
                writer.write("id,type,name,status,description,epic");
                writer.newLine();

                for (Task task : taskHashMap.values()) {
                    writer.write(String.format("%d,%s,%s,%s,%s,",
                            task.getId(), "Epic", task.getName(), task.getStatus(), task.getDescription()));
                    writer.newLine();
                }

                for (Epic epic : epicHashMap.values()) {
                    writer.write(String.format("%d,%s,%s,%s,%s,",
                            epic.getId(), "Epic", epic.getName(), epic.getStatus(), epic.getDescription()));
                    writer.newLine();
                }

                for (SubTask subTask : subTaskHashMap.values()) {
                    writer.write(String.format("%d,%s,%s,%s,%s,%d",
                            subTask.getId(), "Epic", subTask.getName(), subTask.getStatus(), subTask.getDescription(),
                            subTask.getEpic().getId()));
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new ManagerSaveException("Ошибка при сохранении данных в файл.", e);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при создании файла.", e);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }



}
