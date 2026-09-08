package model;

public enum TaskStatus {
    NEW("New"),
    IN_PROGRESS("In progress"),
    DONE("Done"),
    CANCELLED("Cancelled");

    private final String taskStatusTitle;

    TaskStatus(String taskStatusTitle) {
        this.taskStatusTitle = taskStatusTitle;
    }

    public String getTaskStatusTitle() {
        return taskStatusTitle;
    }
}
