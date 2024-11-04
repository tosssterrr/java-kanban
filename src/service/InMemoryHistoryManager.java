package service;

import task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    ArrayList<Task> historyList;

    public InMemoryHistoryManager() {
        this.historyList = new ArrayList<>(10);
    }

    @Override
    public void add(Task task) {
        if (historyList.size() < 10) {
            historyList.add(task);
        } else {
            historyList.removeFirst();
            historyList.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyList;
    }
}
