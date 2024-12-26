package service;

import java.io.File;

public class Managers {

    private Managers() {

    }

    public static TaskManager getFileBacked() {
        File file = new File("tasks.csv");
        return FileBackedTaskManager.loadFromFile(file);
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
