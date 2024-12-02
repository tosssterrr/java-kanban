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
        SubTask movingSubTask3 = new SubTask("Постельное белье",
                "Собрать все белье", TaskStatus.IN_PROGRESS, movingEpic);

        // создаем задачи для тестирования программы
        taskManager.createEpic(movingEpic);
        taskManager.createEpic(birthdayEpic);
        taskManager.createSubTask(movingSubTask1);
        taskManager.createSubTask(movingSubTask2);
        taskManager.createSubTask(movingSubTask3);
        // получаем их для добавления в историю
        taskManager.getSubtask(movingSubTask1.getId());
        taskManager.getSubtask(movingSubTask2.getId());
        taskManager.getEpic(birthdayEpic.getId());
        taskManager.getEpic(movingEpic.getId());
        taskManager.getSubtask(movingSubTask2.getId());
        taskManager.getSubtask(movingSubTask3.getId());
        System.out.println("История: " + taskManager.getHistory());

        // удаляем задачи
        taskManager.deleteEpic(movingEpic.getId());
        // проверяем процесс удаления
        System.out.println(taskManager.getHistory());



    }
}
