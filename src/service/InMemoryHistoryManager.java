package service;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    Map<Integer, Task> historyMap;
    List<Task> historyList;
//    private static final int HISTORY_SIZE = 10;

    public InMemoryHistoryManager() {
        this.historyList = new LinkedList<>();
        this.historyMap = new LinkedHashMap<>();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            historyMap.remove(task.getId());
            historyMap.put(task.getId(), task);
        }
    }

    @Override
    public void remove(int id) {
        historyMap.remove(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(historyMap.values()); // Возвращаем копию списка истории
    }

}