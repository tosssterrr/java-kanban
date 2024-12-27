package task;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final Epic epic;

    public SubTask(String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration, Epic epic) {
        super(name, description, status, startTime, duration);
        this.epic = epic;
        updateEpic();
    }

    public SubTask(int id, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration, Epic epic) {
        super(id, name, description, status, startTime, duration);
        this.epic = epic;
        updateEpic();
    }

    public SubTask(String name, String description, TaskStatus status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
        updateEpic();
    }

    public SubTask(int id, String name, String description, TaskStatus status, Epic epic) {
        super(id, name, description, status);
        this.epic = epic;
        updateEpic();
    }

    @Override
    public void setStatus(TaskStatus status) {
        super.setStatus(status);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epic.name='" + epic.name + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    public Epic getEpic() {
        return epic;
    }

    private void updateEpic() {
        epic.addSubTask(this);
        epic.updateStatus();
    }
}
