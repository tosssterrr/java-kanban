package main;

import service.*;
import task.*;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getFileBacked();
        Epic movingEpic = new Epic("Переезд", "Решение вопросов о переезде");
        Epic birthdayEpic = new Epic("День рождения", "Планируем день рождение");

        SubTask movingSubTask1 = new SubTask("Коробки", "Собрать коробки",
                TaskStatus.NEW, movingEpic);
        SubTask movingSubTask2 = new SubTask("Посуда", "Собрать посуду",
                TaskStatus.NEW, movingEpic);
        SubTask movingSubTask3 = new SubTask("Постельное белье дубликат",
                "Собрать все белье дубликат", TaskStatus.IN_PROGRESS, movingEpic);


        taskManager.createEpic(movingEpic);
        taskManager.createEpic(birthdayEpic);
        taskManager.createSubTask(movingSubTask1);
        taskManager.createSubTask(movingSubTask2);
        taskManager.createSubTask(movingSubTask3);

    }
}
