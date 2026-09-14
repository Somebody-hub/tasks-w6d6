package service;

import model.Task;
import model.TaskPriority;
import model.TaskStatus;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TaskStatistics {

    //Принимает список задач
    //Сортирует по статусу
    //Возвращает отсортированный список
    public Map<TaskStatus, Integer> calculateStatusCounts(List<Task> allTasks) {
        Map<TaskStatus, Integer> statusCount = new EnumMap<>(TaskStatus.class);
        for (TaskStatus taskStatus : TaskStatus.values()) {
            statusCount.put(taskStatus, 0);
        }
        if (allTasks != null) {
            for (Task task : allTasks) {
                statusCount.put(task.getStatus(), statusCount.get(task.getStatus()) + 1);
            }
        }
        return statusCount;
    }

    //Принимает список задач
    //Сортирует по приоритету
    //Возвращает отсортированный список
    public Map<TaskPriority, Integer> calculatePriorityCounts(List<Task> allTasks) {
        Map<TaskPriority, Integer> priorityCounts = new EnumMap<>(TaskPriority.class);
        for (TaskPriority taskPriority : TaskPriority.values()) {
            priorityCounts.put(taskPriority, 0);
        }
        if (allTasks != null) {
            for (Task task : allTasks) {
                priorityCounts.put(task.getPriority(), priorityCounts.get(task.getPriority()) + 1);
            }
        }
        return priorityCounts;
    }

    //Принимает список всех задач
    //Считает процент выполненных задач
    //Возвращает процент выполненных задач
    public double calculateCompletionRate(List<Task> allTasks) {
        if (allTasks == null || allTasks.isEmpty()) return 0.0;
        long doneCount = allTasks.stream()
                .filter(x -> x.getStatus() == TaskStatus.DONE)
                .count();
        return ((double) doneCount / allTasks.size()) * 100.0;
    }

}
