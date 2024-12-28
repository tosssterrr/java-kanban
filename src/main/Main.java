package main;

import service.*;
import task.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getFileBacked();
        Epic movingEpic = new Epic("Переезд", "Решение вопросов о переезде");
        Epic birthdayEpic = new Epic("День рождения", "Планируем день рождение");

        SubTask movingSubTask1 = new SubTask("Коробки", "Собрать коробки",
                TaskStatus.NEW, LocalDateTime.now().plusDays(1), Duration.ofHours(2), movingEpic);
        SubTask movingSubTask2 = new SubTask("Посуда", "Собрать посуду",
                TaskStatus.NEW, LocalDateTime.now().plusHours(28), Duration.ofHours(2), movingEpic);
        SubTask movingSubTask3 = new SubTask("Постельное белье", "Собрать все белье",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ZERO, movingEpic);

        Task task = new Task("Пересекающаяся задача", "Задача для проверки", TaskStatus.NEW,
                LocalDateTime.now().plusDays(1), Duration.ofHours(2));
        taskManager.createEpic(movingEpic);
        taskManager.createEpic(birthdayEpic);
        taskManager.createSubTask(movingSubTask1);
        taskManager.createSubTask(movingSubTask2);
        taskManager.createSubTask(movingSubTask3);
        taskManager.createTask(task);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getPrioritizedTasks());

    }
}
