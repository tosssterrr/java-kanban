package task;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task{
    private ArrayList<SubTask> subTasksList;
    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        subTasksList = new ArrayList<>();
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

    public ArrayList<SubTask> getSubTasksList() {
        return subTasksList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasksList, epic.subTasksList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksList);
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
}
