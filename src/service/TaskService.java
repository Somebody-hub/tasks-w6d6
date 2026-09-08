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
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("The title can not be empty");
        }
        if (priority == null) {
            throw new IllegalArgumentException("Wrong priority");
        }
        Task task = new Task(title, description, priority);
        return repository.save(task);
    }

    //Печатает задачи с листа
    public void printTasks(List<Task> taskList) {
        if (taskList == null || taskList.isEmpty()) {
            System.out.println("Task list is empty.");
            return;
        }
        for (Task task : taskList) {
            System.out.println(task);
        }
    }

    //Печатает все задачи
    public void printAllTasks() {
        List<Task> allTasks = repository.findAll(); //Что сделать 1 вызов в репозитоий
        if (allTasks == null || allTasks.isEmpty()) {
            System.out.println("Task list is empty.");
            return;
        }
        for (Task task : allTasks) {
            System.out.println(task);
        }
    }

    //Поиск в Map по ID
    public Optional<Task> findById(int id) {
        return repository.findById(id);
    }

    //Поиск по названию
    public List<Task> findByTitle(String title) {
        if (title == null || title.isBlank()) return List.of();
        String search = title.trim().toLowerCase();
        return repository.findAll().stream()
                .filter(t -> t.getTitle().toLowerCase().contains(search))
                .toList();
    }

    //Смена статуса
    public boolean changeStatus(int id, TaskStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Invalid status");
        }
        Optional<Task> taskOptional = repository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setStatus(newStatus);
            repository.save(task);
            return true;
        }
        return false;
    }

    //Смена приоритета
    public boolean changePriority(int id, TaskPriority newPriority) {
        if (newPriority == null) {
            throw new IllegalArgumentException("Invalid status");
        }
        Optional<Task> taskOptional = repository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setPriority(newPriority);
            repository.save(task);
            return true;
        }
        return false;
    }

    //Удаление задачи, проверка ID
    public boolean removeTask(int id) {
        return repository.deleteById(id);
    }

    //Добаление тэга
    public boolean addTag(int id, String tag) {
        if (tag == null)
            throw new IllegalArgumentException("Invalid tag");
        if (tag.isBlank())
            return false;
        Optional<Task> taskOptional = repository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            if (task.addTag(tag)) {
                repository.save(task);
                return true;
            }
        }
        return false;
    }

    //Поиск задач по тэгам
    public List<Task> findByTag(String tag) {
        if (tag == null || tag.isBlank()) return List.of();
        String searchTag = tag.trim().toLowerCase();
        List<Task> allTasks = repository.findAll();
        return allTasks.stream()
                .filter(t -> t.getTags().stream().anyMatch(x -> x.equalsIgnoreCase(searchTag)))
                .toList();
    }

    //Расчёт статуса
    public Map<TaskStatus, Integer> countByStatus() {
        return taskStatistics.calculateStatusCounts(repository.findAll());
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
        return String.format(
                "Total tasks: %d | %s: %d | %s: %d | %s: %d (Completion: %.1f%%)",
                allTasks.size(),
                TaskStatus.NEW.getTaskStatusTitle(),
                stats.getOrDefault(TaskStatus.NEW, 0),
                TaskStatus.IN_PROGRESS.getTaskStatusTitle(), stats.getOrDefault(TaskStatus.IN_PROGRESS, 0),
                TaskStatus.DONE.getTaskStatusTitle(), stats.getOrDefault(TaskStatus.DONE, 0),
                taskStatistics.calculateCompletionRate(allTasks)
        );
    }
}
