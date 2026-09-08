import model.*;
import service.*;

import java.util.*;

public class Main {
    private final Scanner sc = new Scanner(System.in);
    private final TaskService taskService = new TaskService();


    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = rdInt("Select option: ");
            switch (choice) {
                case 1 -> handleAddTask();
                case 2 -> handleShowAllTasks();
                case 3 -> handleFindById();
                case 4 -> handleFindByTitle();
                case 5 -> handleChangeStatus();
                case 6 -> handleChangePriority();
                case 7 -> handleDeleteTask();
                case 8 -> handleAddTag();
                case 9 -> handleFindByTag();
                case 10 -> handleShowStatusStat();
                case 11 -> handleSortByPriority();
                case 12 -> handleSortByTitle();
                case 13 -> handleShowProjectSumm();
                case 0 -> {
                    System.out.println("Exiting");
                    running = false;
                }
                default -> System.out.println("Invalid option. Try again");
            }
            if (running && choice >= 1 && choice <= 13) rdString("Press Enter for menu");
        }
    }

    private void printMenu() {
        System.out.println("1. Add task");
        System.out.println("2. Show all tasks");
        System.out.println("3. Find task by ID");
        System.out.println("4. Find task by title");
        System.out.println("5. Change task status");
        System.out.println("6. Change task priority");
        System.out.println("7. Delete task");
        System.out.println("8. Add tag to task");
        System.out.println("9. Show tasks bytag");
        System.out.println("10. Show status statistics");
        System.out.println("11. Sort by priority");
        System.out.println("12. Sort by title");
        System.out.println("13. Show project summary");
        System.out.println("0. Exit");
    }

    private void handleAddTask() {
        System.out.println("--Add new task--");
        String title = rdString("Enter title: ");
        String description = rdString("Enter description");
        TaskPriority taskPriority = selectPriority();
        try {
            Task task = taskService.addTask(title, description, taskPriority);
            System.out.println("Task created successfully. ID: " + task.getId());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    private void handleShowAllTasks() {
        System.out.println("--All tasks--");
        taskService.printAllTasks();
    }

    private void handleFindById() {
        System.out.println("--Find task by ID--");
        int id = rdInt("Enter id");
        Task task = taskService.findById(id);
        if (task != null) System.out.println(task);
        else System.out.println("Task not found");
    }


    private void handleFindByTitle() {
        System.out.println("--Find tasks by title--");
        String title = rdString("Enter title: ");
        List<Task> foundTasks = taskService.findByTitle(title);
        taskService.printTasks(foundTasks);
    }

    private void handleChangeStatus() {
        System.out.println("--Change task status--");
        int id = rdInt("Enter task ID: ");
        TaskStatus taskStatus = selectStatus();
        boolean success = taskService.changeStatus(id, taskStatus);
        if (success) System.out.println("Status updated");
        else System.out.println("Task not found");
    }

    private void handleChangePriority() {
        System.out.println("--Change task priority--");
        int id = rdInt("Enter task ID: ");
        TaskPriority taskPriority = selectPriority();
        boolean success = taskService.changePriority(id, taskPriority);
        if (success) System.out.println("Priority updated");
        else System.out.println("Task not found");
    }

    private void handleDeleteTask() {
        System.out.println("--Delete task--");
        int id = rdInt("Enter task ID: ");
        boolean success = taskService.removeTask(id);
        if (success) System.out.println("Task removed");
        else System.out.println("Task not found");
    }

    private void handleAddTag() {
        System.out.println("--Add tag--");
        int id = rdInt("Enter ID: ");
        String tag = rdString("Enter tag: ");
        boolean success = taskService.addTag(id, tag);
        if (success) System.out.println("Tag added");
        else System.out.println("Task not found or invalid tag");
    }

    private void handleFindByTag() {
        System.out.println("--Find tasks by tag--");
        String tag = rdString("Enter tag: ");
        List<Task> foundTask = taskService.findByTag(tag);
        taskService.printTasks(foundTask);
    }

    private void handleShowStatusStat() {
        System.out.println("--Status statistic--");
        Map<TaskStatus, Integer> stats = taskService.countByStatus();
        for (Map.Entry<TaskStatus, Integer> entry : stats.entrySet()) {
            System.out.println(entry.getKey().getTaskStatusTitle() + " " + entry.getValue() + " tasks");
        }
    }

    private void handleSortByPriority() {
        System.out.println("--Tasks by priority--");
        taskService.printTasks(taskService.sortByPriority());
    }

    private void handleSortByTitle() {
        System.out.println("--Tasks by title--");
        taskService.printTasks(taskService.sortByTitle());
    }

    private void handleShowProjectSumm() {
        System.out.println("--Project summary--");
        System.out.println(taskService.getProjectSummary());
    }

    private int rdInt(String enterString) {
        while (true) {
            System.out.println(enterString);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Try gain");
            }
        }
    }

    private String rdString(String enterString) {
        System.out.println(enterString);
        return sc.nextLine().trim();
    }

    private TaskStatus selectStatus() {
        TaskStatus[] taskStatuses = TaskStatus.values();
        for (int i = 0; i < taskStatuses.length; i++) {
            System.out.println((i + 1) + ". " + taskStatuses[i].getTaskStatusTitle());
        }
        while (true) {
            int choise = rdInt("Select status");
            if (choise <= taskStatuses.length && choise > 0) {
                return taskStatuses[choise - 1];
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
            int choise = rdInt("Select priority");
            if (choise <= taskPriorities.length && choise > 0) {
                return taskPriorities[choise - 1];
            } else {
                System.out.println("Wrong input. Try again");
            }
        }
    }
}