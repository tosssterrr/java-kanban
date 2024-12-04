package service;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private class Node {
        Node prev;
        Task data;
        Node next;

        public Node(Task data) {
            this.data = data;
        }
    }

    private final Map<Integer, Node> historyMap;
    private Node head;
    private Node tail;

    private Node linkLast(Task task) {
        Node node = new Node(task);
        if (tail == null) {
            head = node;
            tail = node;
            node.prev = null;
            node.next = null;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
            tail.next = null;
        }
        return node;
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    private List<Task> getTasksList() {
        List<Task> tasksList = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
            tasksList.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return tasksList;
    }

    public InMemoryHistoryManager() {
        this.historyMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        removeNode(historyMap.remove(task.getId()));

        historyMap.put(task.getId(), linkLast(task));
    }

    @Override
    public void remove(int id) {
        removeNode(historyMap.remove(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasksList(); // Возвращаем копию списка истории
    }

}