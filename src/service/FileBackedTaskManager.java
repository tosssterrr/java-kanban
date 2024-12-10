package service;

import service.exceptions.ManagerSaveException;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static String file_name = "tasks.csv";

    private void save() {
        File file = new File(file_name);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("id,type,name,status,description,epic");
                writer.newLine();

                for (Task task : taskHashMap.values()) {
                    writer.write(String.format("%d,%s,%s,%s,%s,",
                            task.getId(), "Task", task.getName(), task.getStatus(), task.getDescription()));
                    writer.newLine();
                }

                for (Epic epic : epicHashMap.values()) {
                    writer.write(String.format("%d,%s,%s,%s,%s,",
                            epic.getId(), "Epic", epic.getName(), epic.getStatus(), epic.getDescription()));
                    writer.newLine();
                }

                for (SubTask subTask : subTaskHashMap.values()) {
                    writer.write(String.format("%d,%s,%s,%s,%s,%d",
                            subTask.getId(), "SubTask", subTask.getName(), subTask.getStatus(), subTask.getDescription(),
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

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileManager = new FileBackedTaskManager();
        file_name = file.getName();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",");

                int id = Integer.parseInt(parts[0]);
                String type = parts[1];

                switch (type) {
                    case "Epic":
                        Epic epic = new Epic(id, parts[2], parts[4], TaskStatus.valueOf(parts[3]));
                        fileManager.createEpic(epic);
                        break;
                    case "SubTask":
                        Epic epicForSubTask = fileManager.getEpic(Integer.parseInt(parts[5]));
                        SubTask subTask = new SubTask(id, parts[2], parts[4], TaskStatus.valueOf(parts[3]),
                                epicForSubTask);
                        fileManager.createSubTask(subTask);
                        break;
                    case "Task":
                        Task task = new Task(id, parts[2], parts[4], TaskStatus.valueOf(parts[3]));
                        fileManager.createTask(task);
                        break;
                }
            }
        } catch (IOException e) {
            return new FileBackedTaskManager();
        }
        return fileManager;
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
