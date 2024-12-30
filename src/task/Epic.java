package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class Epic extends Task {
    private final ArrayList<SubTask> subTasksList;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        subTasksList = new ArrayList<>();
    }

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
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

        boolean allDone = true;
        boolean anyInProgressOrDone = false;

        for (SubTask subTask : subTasksList) {
            if (subTask.getStatus() == TaskStatus.IN_PROGRESS) {
                anyInProgressOrDone = true;
                allDone = false;
            } else if (subTask.getStatus() != TaskStatus.DONE) {
                allDone = false;
            }
        }

        if (anyInProgressOrDone) {
            setStatus(TaskStatus.IN_PROGRESS);
        } else if (allDone) {
            setStatus(TaskStatus.DONE);
        } else {
            setStatus(TaskStatus.NEW);
        }
    }

    public void addSubTask(SubTask subTask) {
        subTasksList.add(subTask);
        startTime = getSubTasksList().stream()
                .map(SubTask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        duration = Optional.ofNullable(duration)
                .orElse(Duration.ZERO).plus(subTask.duration != null ? subTask.duration : Duration.ZERO);
    }

    public void deleteSubTask(SubTask subTask) {
        subTasksList.remove(subTask);
        startTime = getSubTasksList().stream()
                .map(SubTask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        duration = Optional.ofNullable(duration)
                .map(d -> d.minus(subTask.duration != null ? subTask.duration : Duration.ZERO))
                .orElse(Duration.ZERO);
    }

    @Override
    public LocalDateTime getEndTime() {
        return getSubTasksList().stream()
                .map(SubTask::getStartTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);
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
