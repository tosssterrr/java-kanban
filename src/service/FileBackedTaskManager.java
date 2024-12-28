package service;

import service.exceptions.ManagerSaveException;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static String file_name = "tasks.csv";
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");

    private void save() {
        File file = new File(file_name);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("id,type,name,status,description,startTime,duration,epic");
                writer.newLine();

                for (Task task : taskHashMap.values()) {
                    writer.write(toString(task));
                    writer.newLine();
                }

                for (Epic epic : epicHashMap.values()) {
                    writer.write(toString(epic));
                    writer.newLine();
                }

                for (SubTask subTask : subTaskHashMap.values()) {
                    writer.write(toString(subTask));
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

                final int id = Integer.parseInt(parts[0]);
                final String type = parts[1];
                final String name = parts[2];
                final TaskStatus status = TaskStatus.valueOf(parts[3]);
                final String description = parts[4];
                final LocalDateTime startTime = parts[5].equals(" ") ?
                        null : LocalDateTime.parse(parts[5], DATE_TIME_FORMAT);
                final Duration duration = parts[6].equals(" ") ?
                        null : Duration.ofMinutes(Long.parseLong(parts[6]));
                switch (type) {
                    case "Epic":
                        Epic epic = new Epic(id, name, description, status);
                        fileManager.createEpic(epic);
                        break;
                    case "SubTask":
                        Epic epicForSubTask = fileManager.getEpic(Integer.parseInt(parts[7]));
                        SubTask subTask = new SubTask(id, name, description, status,
                                startTime, duration, epicForSubTask);
                        fileManager.createSubTask(subTask);
                        break;
                    case "Task":
                        Task task = new Task(id, name, description, status, startTime, duration);
                        fileManager.createTask(task);
                        break;
                }
            }
        } catch (IOException e) {
            return new FileBackedTaskManager();
        }
        return fileManager;
    }

    private static String toString(Task task) {
        String type = task instanceof SubTask ? "SubTask" : (task instanceof Epic ? "Epic" : "Task");
        String epicId = (task instanceof SubTask) ? String.valueOf(((SubTask) task).getEpic().getId()) : " ";
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                task.getId(), type, task.getName(), task.getStatus(), task.getDescription(),
                task.getStartTime() != null ? task.getStartTime().format(DATE_TIME_FORMAT) : " ",
                task.getDuration() != null ? task.getDuration().toMinutes() : " ", epicId);
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
