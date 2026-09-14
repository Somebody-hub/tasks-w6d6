import model.*;
import repository.*;
import service.*;

import java.util.*;

public class Main {
    private final Scanner sc;
    private final TaskService taskService;

    public Main() {
        Repository<Task, Integer> repository = new LocalRepository<>();
        this.taskService = new TaskService(repository);
        this.sc = new Scanner(System.in);
    }

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        boolean running = true;
        try {
            while (running) {
                printMenu();
                int choice = rdInt("Select option: ");
                switch (choice) {
                    case 1 -> handleAddTask();
                    case 2 -> handleShowAllTasks();
                    case 3 -> handleFindById();
                    case 4 -> handleFindByTitle();
                    case 5 -> handleChangeDescription();
                    case 6 -> handleChangeStatus();
                    case 7 -> handleChangePriority();
                    case 8 -> handleDeleteTask();
                    case 9 -> handleAddTag();
                    case 10 -> handleFindByTag();
                    case 11 -> handleShowStatusStat();
                    case 12 -> handleShowPriorityStats();
                    case 13 -> handleSortByPriority();
                    case 14 -> handleSortByTitle();
                    case 15 -> handleShowProjectSumm();
                    case 0 -> {
                        System.out.println("Exiting");
                        running = false;
                    }
                    default -> System.out.println("Invalid option. Try again");
                }
                if (running) rdString("Press Enter for menu");
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nInput canceled. Exiting...");
        }
    }


    private void printMenu() {
        System.out.println("1. Add task");
        System.out.println("2. Show all tasks");
        System.out.println("3. Find task by ID");
        System.out.println("4. Find task by title");
        System.out.println("5. Set new task description");
        System.out.println("6. Change task status");
        System.out.println("7. Change task priority");
        System.out.println("8. Delete task");
        System.out.println("9. Add tag to task");
        System.out.println("10. Show tasks by tag");
        System.out.println("11. Show status statistics");
        System.out.println("12. Show priority statistics");
        System.out.println("13. Sort by priority");
        System.out.println("14. Sort by title");
        System.out.println("15. Show project summary");
        System.out.println("0. Exit");
    }

    private void handleAddTask() {
        System.out.println("--Add new task--");
        String title = rdString("Enter title: ");
        String description = rdString("Enter description");
        TaskPriority taskPriority = selectPriority();
        try {
            Task task = taskService.addTask(title, description, taskPriority); //DTO
            System.out.println("Task created successfully. ID: " + task.getId());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    private void handleShowAllTasks() {
        System.out.println("--All tasks--");
        List<Task> allTasks = taskService.getAllTasks(); //DTO
        if (allTasks == null || allTasks.isEmpty()) {
            System.out.println("Task list is empty.");
            return;
        }
        for (Task task : allTasks) {
            System.out.println(task);
        }
    }

    private void handleFindById() {
        System.out.println("--Find task by ID--");
        int id = rdInt("Enter id");
        Optional<Task> task = taskService.findById(id); //DTO
        if (task.isPresent()) System.out.println(task.get());
        else System.out.println("Task ID" + id + "not found");
    }


    private void handleFindByTitle() {
        System.out.println("--Find tasks by title--");
        String title = rdString("Enter title: ");
        try {
            List<Task> foundTasks = taskService.findByTitle(title); //DTO
            printTasks(foundTasks);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleChangeDescription() {
        System.out.println("--Change description--");
        int id = rdInt("Enter task ID: ");
        if (taskService.existById(id)) {
            System.out.println("Task ID" + id + "not found");
            return;
        }
        String description = rdString("Enter new description: ");
        try {
            taskService.changeDescription(id, description);
            System.out.println("Description updated");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleChangeStatus() {
        System.out.println("--Change task status--");
        int id = rdInt("Enter task ID: ");
        if (taskService.existById(id)) {
            System.out.println("Task ID" + id + "not found");
            return;
        }
        TaskStatus taskStatus = selectStatus();
        try {
            taskService.changeStatus(id, taskStatus);
            System.out.println("Status updated");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleChangePriority() {
        System.out.println("--Change task priority--");
        int id = rdInt("Enter task ID: ");
        if (taskService.existById(id)) {
            System.out.println("Task ID" + id + "not found");
            return;
        }
        TaskPriority taskPriority = selectPriority();
        try {
            taskService.changePriority(id, taskPriority);
            System.out.println("Priority updated");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleDeleteTask() {
        System.out.println("--Delete task--");
        int id = rdInt("Enter task ID: ");
        if (taskService.existById(id)) {
            System.out.println("Task ID" + id + "not found");
            return;
        }
        boolean success = taskService.removeTask(id);
        if (success) System.out.println("Task removed");
        else System.out.println("Task not found");
    }

    private void handleAddTag() {
        System.out.println("--Add tag--");
        int id = rdInt("Enter ID: ");
        if (taskService.existById(id)) {
            System.out.println("Task ID" + id + "not found");
            return;
        }
        String tag = rdString("Enter tag: ");
        try {
            boolean success = taskService.addTag(id, tag);
            if (success) System.out.println("Tag added");
            else System.out.println("This tag already exists");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleFindByTag() {
        System.out.println("--Find tasks by tag--");
        String tag = rdString("Enter tag: ");
        List<Task> foundTask = taskService.findByTag(tag); //DTO
        printTasks(foundTask);
    }

    private void handleShowStatusStat() {
        System.out.println("--Status statistic--");
        Map<TaskStatus, Integer> stats = taskService.countByStatus(); //DTO
        for (Map.Entry<TaskStatus, Integer> entry : stats.entrySet()) {
            System.out.println(entry.getKey().getTaskStatusTitle() + " " + entry.getValue() + " tasks");
        }
    }

    private void handleShowPriorityStats() {
        System.out.println("--Priority statistic--");
        Map<TaskPriority, Integer> stats = taskService.countByPriority(); //DTO
        for (Map.Entry<TaskPriority, Integer> entry : stats.entrySet()) {
            System.out.println(entry.getKey().getPriorityTitle() + " " + entry.getValue() + " tasks");
        }
    }


    private void handleSortByPriority() {
        System.out.println("--Tasks by priority--");
        printTasks(taskService.sortByPriority()); //DTO
    }

    private void handleSortByTitle() {
        System.out.println("--Tasks by title--");
        printTasks(taskService.sortByTitle()); //DTO
    }

    private void handleShowProjectSumm() {
        System.out.println("--Project summary--");
        System.out.println(taskService.getProjectSummary()); //DTO
    }

    private int rdInt(String enterString) {
        while (true) {
            System.out.println(enterString);
            if (!sc.hasNextLine()) {
                throw new NoSuchElementException();
            }
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Try again");
            }
        }
    }

    private String rdString(String enterString) {
        System.out.println(enterString);
        if (!sc.hasNextLine()) {
            throw new NoSuchElementException();
        }
        return sc.nextLine().trim();
    }

    private TaskStatus selectStatus() {
        TaskStatus[] taskStatuses = TaskStatus.values();
        for (int i = 0; i < taskStatuses.length; i++) {
            System.out.println((i + 1) + ". " + taskStatuses[i].getTaskStatusTitle());
        }
        while (true) {
            int choose = rdInt("Select status");
            if (choose <= taskStatuses.length && choose > 0) {
                return taskStatuses[choose - 1];
            } else {
                System.out.println("Wrong input. Try again");
            }
        }
    }

    private TaskPriority selectPriority() {
        TaskPriority[] taskPriorities = TaskPriority.values();
        for (int i = 0; i < taskPriorities.length; i++) {
            System.out.println((i + 1) + ". " + taskPriorities[i].getPriorityTitle());
        }
        while (true) {
            int choose = rdInt("Select priority");
            if (choose <= taskPriorities.length && choose > 0) {
                return taskPriorities[choose - 1];
            } else {
                System.out.println("Wrong input. Try again");
            }
        }
    }

    public void printTasks(List<Task> taskList) {
        if (taskList == null || taskList.isEmpty()) {
            System.out.println("No tasks found");
            return;
        }
        for (Task task : taskList) {
            System.out.println(task);
        }
    }

}