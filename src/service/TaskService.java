package service;

import comparator.*;
import model.*;
import repository.*;

import java.util.*;

public class TaskService {
    private final Repository<Task, Integer> repository;

    public TaskService(Repository<Task, Integer> repository) {
        this.repository = repository;
    }

    private final TaskStatistics taskStatistics = new TaskStatistics();

    //Добавляет задачу проверяет значения
    public Task addTask(String title, String description, TaskPriority priority) {
        Task task = new Task(title, description, priority);
        repository.save(task);
        return task;
    }

    //Печатает все задачи
    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    //Поиск в Map по ID
    public Optional<Task> findById(int id) {
        return repository.findById(id);
    }

    public boolean existById(int id) {
        return !repository.existsById(id);
    }

    //Поиск по названию
    public List<Task> findByTitle(String title) {
        if (title == null)
            throw new IllegalArgumentException("Invalid title");
        if (title.isBlank())
            throw new IllegalArgumentException("Search query cannot be empty");
        String search = title.trim().toLowerCase();
        return repository.findAll().stream()
                .filter(t -> t.getTitle().toLowerCase().contains(search))
                .toList();
    }

    //Смена статуса
    public void changeStatus(int id, TaskStatus newStatus) {
        Optional<Task> taskOptional = repository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setStatus(newStatus);
            repository.save(task);
        }
    }

    //Установка нового описания задачи
    public void changeDescription(int id, String description) {
        Optional<Task> taskOptional = repository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setDescription(description);
            repository.save(task);
        }
    }

    //Смена приоритета
    public void changePriority(int id, TaskPriority newPriority) {
        Optional<Task> taskOptional = repository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setPriority(newPriority);
            repository.save(task);
        }
    }

    //Удаление задачи, проверка ID
    public boolean removeTask(int id) {
        return repository.deleteById(id);
    }

    //Добаление тэга
    public boolean addTag(int id, String tag) {
        Optional<Task> taskOptional = repository.findById(id);
        if (taskOptional.isEmpty())
            throw new IllegalArgumentException("Task with ID " + id + " not found");
        Task task = taskOptional.get();
        if (task.addTag(tag)) {
            repository.save(task);
            return true;
        }
        return false;
    }

    //Поиск задач по тэгам
    public List<Task> findByTag(String tag) {
        if (tag == null || tag.isBlank()) return List.of();
        String searchTag = tag.trim().toLowerCase();
        List<Task> allTasks = repository.findAll();
        return allTasks.stream()
                .filter(t -> t.getTags().contains(searchTag))
                .toList();
    }

    //Расчёт статистики статуса
    public Map<TaskStatus, Integer> countByStatus() {
        return taskStatistics.calculateStatusCounts(repository.findAll());
    }

    //Расчёт статистики приоритетов
    public Map<TaskPriority, Integer> countByPriority() {
        return taskStatistics.calculatePriorityCounts(repository.findAll());
    }

    //Возвращает отсортиованный лист по приоритету
    public List<Task> sortByPriority() {
        return repository.findAll().stream()
                .sorted(new TaskPriorityComparator())
                .toList();
    }

    //Возвращает лист отсортированный по названиям
    public List<Task> sortByTitle() {
        return repository.findAll().stream()
                .sorted(new TaskTitleComparator())
                .toList();
    }


    //Вывод общей статистики
    public String getProjectSummary() {
        List<Task> allTasks = repository.findAll();
        Map<TaskStatus, Integer> stats = countByStatus();
        StringBuilder resultString = new StringBuilder();

        resultString.append("Total tasks: ").append(allTasks.size());
        for (TaskStatus taskStatus : TaskStatus.values()) {
            resultString.append(" | ");
            resultString.append(taskStatus.getTaskStatusTitle());
            resultString.append(": ");
            resultString.append(stats.getOrDefault(taskStatus, 0));
        }
        resultString.append(String.format(" ||(Completion: %.1f%%)", taskStatistics.calculateCompletionRate(allTasks)));
        return resultString.toString();
    }


}
