package service;

import task.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    List<Task> historyList;
    private static final int HISTORY_SIZE = 10;

    public InMemoryHistoryManager() {
        this.historyList = new LinkedList<>();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            if (historyList.size() < HISTORY_SIZE) {
                historyList.add(task);
            } else {
                historyList.removeFirst();
                historyList.add(task);
            }
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(historyList); // Возвращаем копию списка истории
    }

}
