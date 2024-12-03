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

    private void linkLast(Node node) {
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
    }

    private void removeNode(Node node) {
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
        historyMap.remove(node.data.getId());
    }

    private List<Task> getTasksList() {
        List<Task> tasksList = new ArrayList<>();
        for (Node currentNode = head; currentNode != null; currentNode = currentNode.next) {
            tasksList.add(currentNode.data);
        }
        return tasksList;
    }

    public InMemoryHistoryManager() {
        this.historyMap = new HashMap<>();
        this.head = null;
        this.tail = null;
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            if (historyMap.containsKey(task.getId())) {
                removeNode(historyMap.get(task.getId()));
            }
            Node newNode = new Node(task);

            linkLast(newNode);

            historyMap.put(task.getId(), newNode);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(historyMap.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasksList(); // Возвращаем копию списка истории
    }

}