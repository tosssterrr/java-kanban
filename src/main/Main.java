package main;

import service.TaskManager;
import task.*;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Epic movingEpic = new Epic("Переезд", "Решение вопросов о переезде");
        Epic birthdayEpic = new Epic("День рождения", "Планируем день рождение");

        SubTask movingSubTask1 = new SubTask("Коробки", "Собрать коробки",
                TaskStatus.NEW, movingEpic);
        SubTask movingSubTask2 = new SubTask("Посуда", "Собрать посуду",
                TaskStatus.NEW, movingEpic);
        SubTask birthdaySubTask1 = new SubTask("Список гостей",
                "Написать список приглашенных гостей", TaskStatus.IN_PROGRESS, birthdayEpic);

        Task task1 = new Task("Покупка мебели", "Купить новую мебель для квартиры",
                TaskStatus.IN_PROGRESS);

        taskManager.createEpic(movingEpic);
        taskManager.createEpic(birthdayEpic);
        taskManager.createSubTask(movingSubTask1);
        taskManager.createSubTask(movingSubTask2);
        taskManager.createSubTask(birthdaySubTask1);
        taskManager.createTask(task1);

        System.out.println("Задачи: " + taskManager.getAllTasks());
        System.out.println("Подзадачи: " + taskManager.getAllSubtasks());
        System.out.println("Эпики: " + taskManager.getAllEpics());

        task1.setStatus(TaskStatus.DONE);
        movingSubTask1.setStatus(TaskStatus.DONE);
        movingSubTask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task1);
        taskManager.updateSubTask(movingSubTask1);
        taskManager.updateSubTask(movingSubTask2);

        movingEpic.setDescription("Задачи для переезда");
        taskManager.updateEpic(movingEpic);

        System.out.println("Статус 1-ой задачи - " + task1.getStatus());
        System.out.println("Статус 1-ой подзадачи переезда - " + movingSubTask1.getStatus());
        System.out.println("Статус 2-ой подзадачи переезда - " + movingSubTask2.getStatus());
        System.out.println("Статус эпика переезда - " + movingEpic.getStatus());

        taskManager.deleteTask(task1.getId());
        taskManager.deleteEpic(movingEpic.getId());

        System.out.println("Задачи: " + taskManager.getAllTasks());
        System.out.println("Подзадачи: " + taskManager.getAllSubtasks());
        System.out.println("Эпики: " + taskManager.getAllEpics());
    }
}
