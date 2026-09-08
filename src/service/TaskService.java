package service;

import comparator.TaskPriorityComparator;
import comparator.TaskTitleComparator;
import model.Task;
import model.TaskPriority;
import model.TaskStatus;

import java.util.*;

public class TaskService {
    List<Task> tasks = new ArrayList<>();
    Map<Integer, Task> taskById = new HashMap<>();

    private final TaskStatistics taskStatistics = new TaskStatistics();

    public Task addTask(String title, String description, TaskPriority priority) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("The title can not be empty");
        }
        if (priority == null) {
            throw new IllegalArgumentException("Wrong priority");
        }
        Task task = new Task(title, description, priority);
        tasks.add(task);
        taskById.put(task.getId(), task);
        return task;
    }

    public void printTasks(List<Task> taskList) {
        if (taskList == null || taskList.isEmpty()) {
            System.out.println("Task list is empty.");
            return;
        }
        for (Task task : taskList) {
            System.out.println(task);
        }
    }

    public void printAllTasks() {
        if (tasks == null || tasks.isEmpty()) {
            System.out.println("Task list is empty.");
            return;
        }
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    public Task findById(int id) {
        return taskById.getOrDefault(id, null);
    }

    public List<Task> findByTitle(String title) {
        if (title == null || title.isBlank()) return List.of();
        String search = title.trim().toLowerCase();
        return tasks.stream()
                .filter(t -> t.getTitle().toLowerCase().contains(search))
                .toList();
    }

    public boolean changeStatus(int id, TaskStatus newStatus) {
        Task task = taskById.get(id);
        if (task == null || newStatus == null) return false;
        task.setStatus(newStatus);
        return true;
    }

    public boolean changePriority(int id, TaskPriority newPriority) {
        Task task = taskById.get(id);
        if (task == null || newPriority == null) return false;
        task.setPriority(newPriority);
        return true;
    }

    public boolean removeTask(int id) {
        Task task = taskById.get(id);
        if (task == null) return false;
        tasks.remove(task);
        taskById.remove(id);
        return true;
    }

    public boolean addTag(int id, String tag) {
        Task task = taskById.get(id);
        if (task == null || tag == null || tag.isBlank()) return false;
        task.addTag(tag);
        return true;
    }

    public List<Task> findByTag(String tag) {
        if (tag == null || tag.isBlank()) return List.of();
        String searchTag = tag.trim().toLowerCase();
        return tasks.stream()
                .filter(t -> t.getTags().stream().anyMatch(x -> x.equalsIgnoreCase(searchTag)))
                .toList();
    }

    public Map<TaskStatus, Integer> countByStatus() {
        return taskStatistics.calculateStatusCounts(tasks);
    }

    public List<Task> sortByPriority() {
        return tasks.stream()
                .sorted(new TaskPriorityComparator())
                .toList();
    }

    public List<Task> sortByTitle() {
        return tasks.stream()
                .sorted(new TaskTitleComparator())
                .toList();
    }

    public String getCompletionRate() {
        return String.format("Completion %.1f%% \n", taskStatistics.calculateCompletionRate(tasks));
    }

    public String getProjectSummary() {
        Map<TaskStatus, Integer> stats = countByStatus();
        return String.format(
                "Total tasks: %d | %s: %d | %s: %d | %s: %d (Completion: %.1f%%)",
                tasks.size(),
                TaskStatus.NEW.getTaskStatusTitle(),
                stats.getOrDefault(TaskStatus.NEW, 0),
                TaskStatus.IN_PROGRESS.getTaskStatusTitle(), stats.getOrDefault(TaskStatus.IN_PROGRESS, 0),
                TaskStatus.DONE.getTaskStatusTitle(), stats.getOrDefault(TaskStatus.DONE, 0),
                taskStatistics.calculateCompletionRate(tasks)
        );
    }
}
