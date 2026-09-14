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

    //Принимает название задачи title, описание задачи description, приоритет задачи priority
    //Создаёт объект Task, сохраняет его в репозитории
    //Возвращает ссылку на созданую задачу
    public Task addTask(String title, String description, TaskPriority priority) {
        Task task = new Task(title, description, priority);
        repository.save(task);
        return task;
    }

    //Ничего не принимает
    //Обращается к репозиторию за списком всех задач
    //Возвращает список всех задач
    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    //Принимает id задачи
    //Вызывает репозиторий для поиска задачи по id. Передаёт id в репозитории
    //Возвращает обёртку Optional (не)найденого объекта
    public Optional<Task> findById(int id) {
        return repository.findById(id);
    }

    //Принимает id задачи
    //Вызывает репозиторий для поиска задачи по id. Передаёт id в репозитории
    //Возвращает boolean, находитсся ли задача в коллекции
    public boolean existById(int id) {
        return !repository.existsById(id);
    }

    //Приниает строку название задачи
    //Обращается к репозиторию и получает список всех задач
    //Ищет совпадение title строки с названиями задач в списке
    //Возвращает список найденых задач
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

    //Принимает id задач и новый статус
    //Ищет задачу в репозитории. Если задача найдена, меняет статус на новый. Сохраняет изменения
    //Ничего не возвращает
    public void changeStatus(int id, TaskStatus newStatus) {
        Optional<Task> taskOptional = repository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setStatus(newStatus);
            repository.save(task);
        }
    }

    //Принимает id задач и новое описание
    //Ищет задачу в репозитории. Если задача найдена, меняет описание на новое. Сохраняет изменения
    //Ничего не возвращает
    public void changeDescription(int id, String description) {
        Optional<Task> taskOptional = repository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setDescription(description);
            repository.save(task);
        }
    }

    //Принимает id задач и новый приоритет
    //Ищет задачу в репозитории. Если задача найдена, меняет приоритет на новый. Сохраняет изменения
    //Ничего не возвращает
    public void changePriority(int id, TaskPriority newPriority) {
        Optional<Task> taskOptional = repository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setPriority(newPriority);
            repository.save(task);
        }
    }

    //Принимает id задачи
    //Вызывает метод удаления задачи в репозитории
    //Возвращает упешность удаления
    public boolean removeTask(int id) {
        return repository.deleteById(id);
    }

    //Принимает id задачи и строку тэг, которую необходимо добавить
    //Ищет задачу по id в репозитории. Добавляет ей тэг. Сохраняет изменения в репозиторий
    //Возвращает успешность выполнения
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

    //Принимает строку тэг
    //Получает список всех задач. Перебирает список тэгов каждой задачи.
    //Возвращает список найдеых задач
    public List<Task> findByTag(String tag) {
        if (tag == null || tag.isBlank()) return List.of();
        String searchTag = tag.trim().toLowerCase();
        List<Task> allTasks = repository.findAll();
        return allTasks.stream()
                .filter(t -> t.getTags().contains(searchTag))
                .toList();
    }

    //Ничего не принимает
    //Расчитывает статистику задач по статусу
    //Возвращает коллекцию найденных задач
    public Map<TaskStatus, Integer> countByStatus() {
        return taskStatistics.calculateStatusCounts(repository.findAll());
    }

    //Ничего не принимает
    //Расчитывает статистику задач по приоритету
    //Возвращает коллекцию найденных задач
    public Map<TaskPriority, Integer> countByPriority() {
        return taskStatistics.calculatePriorityCounts(repository.findAll());
    }

    //Ничего не принимает
    //Возвращает отсортированный список задач по приоритету
    //Возвращает отсортированный список
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


    //Ничего не принимает
    //Собирает строку из данных всех задач
    //Возвращает строку общей статистики
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
