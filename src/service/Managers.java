package service;

public class Managers {
    private static final HistoryManager defaultHistoryManager = new InMemoryHistoryManager();
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory() {
        return defaultHistoryManager;
    }
}
