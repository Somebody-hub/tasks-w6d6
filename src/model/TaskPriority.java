package model;

public enum TaskPriority {
    LOW("Low", 1),
    MEDIUM("Medium", 2),
    HIGH("High", 3),
    CRITICAL("Critical", 4);

    private final String priorityTitle;
    private final int priorityWeight;

    TaskPriority(String priorityTitle, int priorityWeight) {
        this.priorityTitle = priorityTitle;
        this.priorityWeight = priorityWeight;
    }

    public String getPriorityTitle() {
        return priorityTitle;
    }

    public int getPriorityWeight() {
        return priorityWeight;
    }
}
