package main;

import service.*;
import task.*;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
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
        // создаем задачи для тестирования программы
        taskManager.createEpic(movingEpic);
        taskManager.createEpic(birthdayEpic);
        taskManager.createSubTask(movingSubTask1);
        taskManager.createSubTask(movingSubTask2);
        taskManager.createSubTask(birthdaySubTask1);
        taskManager.createTask(task1);
        // получаем их для добавления в историю
        taskManager.getTask(task1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getSubtask(movingSubTask1.getId());
        taskManager.getSubtask(movingSubTask2.getId());
        taskManager.getEpic(birthdayEpic.getId());
        taskManager.getEpic(movingEpic.getId());



        System.out.println("Задачи: " + taskManager.getTasks());
        System.out.println("Подзадачи: " + taskManager.getSubtasks());
        System.out.println("Эпики: " + taskManager.getEpics());


        // обновляем статусы и меняем описание
        task1.setStatus(TaskStatus.DONE);
        movingSubTask1.setStatus(TaskStatus.DONE);
        movingSubTask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task1);
        taskManager.updateSubTask(movingSubTask1);
        taskManager.updateSubTask(movingSubTask2);
        movingEpic.setDescription("Задачи для переезда");
        taskManager.updateEpic(movingEpic);
        // проверяем действительно ли все изменилось
        System.out.println("Статус 1-ой задачи - " + task1.getStatus());
        System.out.println("Статус 1-ой подзадачи переезда - " + movingSubTask1.getStatus());
        System.out.println("Статус 2-ой подзадачи переезда - " + movingSubTask2.getStatus());
        System.out.println("Статус эпика переезда - " + movingEpic.getStatus());
        // удаляем задачи
        taskManager.deleteTask(task1.getId());
        taskManager.deleteEpic(movingEpic.getId());

        // проверяем процесс удаления
        System.out.println("Задачи: " + taskManager.getTasks());
        System.out.println("Подзадачи: " + taskManager.getSubtasks());
        System.out.println("Эпики: " + taskManager.getEpics());

        System.out.println(taskManager.getHistory());



    }
}
