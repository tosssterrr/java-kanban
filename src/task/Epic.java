package task;

import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<SubTask> subTasksList;
    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        subTasksList = new ArrayList<>();
    }

    public ArrayList<SubTask> getSubTasksList() {
        return subTasksList;
    }

    public void updateStatus() {
        if (subTasksList.isEmpty()) {
            setStatus(TaskStatus.NEW);
            return;
        }

        boolean allNew = true;
        boolean anyInProgressOrDone = false;

        for (SubTask subTask : subTasksList) {
            if (subTask.getStatus() == TaskStatus.DONE) {
                anyInProgressOrDone = true;
                allNew = false;
            } else if (subTask.getStatus() == TaskStatus.IN_PROGRESS) {
                anyInProgressOrDone = true;
                allNew = false;
            }
        }

        if (allNew) {
            setStatus(TaskStatus.NEW);
        } else if (anyInProgressOrDone) {
            setStatus(TaskStatus.IN_PROGRESS);
        } else {
            setStatus(TaskStatus.DONE);
        }
    }

    public void addSubTask(SubTask subTask) {
        subTasksList.add(subTask);
    }

    public void deleteSubTask(SubTask subTask) {
        subTasksList.remove(subTask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subTasksList=" + subTasksList +
                '}';
    }
}
