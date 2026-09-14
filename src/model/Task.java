package model;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class Task implements Identifiable<Integer> {
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private final int id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private final Set<String> tags;

    public Task(String title, String description, TaskPriority taskPriority) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("The title can not be empty");
        if (taskPriority == null) throw new IllegalArgumentException("Wrong priority");
        this.id = idCounter.getAndIncrement();
        this.title = title.trim();
        this.description = description == null ? "" : description.trim();
        this.priority = taskPriority;
        this.status = TaskStatus.NEW;
        this.tags = new HashSet<>();
    }

    @Override
    public Integer getId() {
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
        if (title == null)
            throw new IllegalArgumentException("Invalid title");
        if (title.isBlank())
            throw new IllegalArgumentException("The title cannot be empty");
        this.title = title.trim();
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }

    public void setPriority(TaskPriority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Invalid priority");
        }
        this.priority = priority;
    }

    public void setStatus(TaskStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Invalid status");
        }
        this.status = status;
    }

    public boolean addTag(String tag) {
        if (tag == null)
            throw new IllegalArgumentException("Invalid tag");
        if (tag.isBlank())
            throw new IllegalArgumentException("The tag cannot be empty");
        return this.tags.add(tag.trim().toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Task task)) return false;
        return Objects.equals(task.getId(), this.getId());
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
