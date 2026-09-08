package model;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Task {
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private final int id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private final Set<String> tags;

    public Task(String title, String description, TaskPriority taskPriority) {
        this.id = idCounter.getAndIncrement();
        this.title = title == null ? "" : title.trim();
        this.description = description == null ? "" : description.trim();
        this.priority = taskPriority;
        this.status = TaskStatus.NEW;
        this.tags = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Set<String> getTags() {
        return new HashSet<>(tags);
    }

    public void setTitle(String title) {
        if (title != null || !title.isBlank()) {
            this.title = title.trim();
        }
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description.trim();
        }
    }

    public void setPriority(TaskPriority priority) {
        if (priority != null) {
            this.priority = priority;
        }
    }

    public void setStatus(TaskStatus status) {
        if (status != null) {
            this.status = status;
        }
    }

    public boolean addTag(String tag) {
        if (tag == null || tag.isBlank()) return false;
        return this.tags.add(tag.trim().toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        Task task = (Task) o;
        return task.id == this.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String tagList = tags.isEmpty() ? "None" : String.join(", ", tags);
        return String.format(
                "Task #%d [%s | Priority: %s] %s — %s (Tags: %s)",
                id,
                status.getTaskStatusTitle(),
                priority.getPriorityTitle(),
                title,
                description,
                tagList
        );
    }

}
