package org.example.view;

import org.example.controller.TaskManager;
import org.example.model.DatabaseHelper;
import org.example.model.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

// This is what is printed to the user. Since this is a console application, this class wasn't completely necessary, but
// I added it to show I have an understanding of views and to help keep the logic and UI separate and more readable.

public class ConsoleView {
    private final Scanner scanner = new Scanner(System.in);
    private final TaskManager taskManager;
    private final DatabaseHelper dbHelper;

    public ConsoleView(TaskManager taskManager, DatabaseHelper dbHelper) {
        this.taskManager = taskManager;
        this.dbHelper = dbHelper;
    }

    // this is the first menu the user will see
    public void showMainMenu() {
        while (true) {
            // Prints the menu for the user, user enters a number, the action corresponding to that number is run
            System.out.println("\n==== Task Manager Menu ====");
            System.out.println("1. Add Task");
            System.out.println("2. View Task");
            System.out.println("3. Edit Task");
            System.out.println("4. Delete Task");
            System.out.println("0. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addTask();
                case "2" -> viewTask();
                case "3" -> editTask();
                case "4" -> deleteTask();
                case "0" -> {
                    System.out.println("Exiting...");
                    dbHelper.closeConnection();
                    return;
                }
                default -> System.out.println("Invalid input");
            }
        }
    }

    // this will guide the user through adding a task - the user set the title, date, and priority level.
    // The completion status is defaulted to false and the ID is auto-incremented so the user doesn't set those here.
    private void addTask() {
        System.out.println("Enter the task title:");
        String title = scanner.nextLine();

        // Getting the priority from the user (assuming it's a number from 1 to 5)
        int priority;
        while (true) {
            try {
                System.out.println("Enter the task priority (1 being highest, to 3 being lowest):");
                priority = Integer.parseInt(scanner.nextLine());
                if (priority < 1 || priority > 3) {
                    System.out.println("Priority must be between 1 and 3. Please try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid priority. Please enter a number between 1 and 3.");
            }
        }

        // Getting the due date from the user
        LocalDate dueDate = null;
        while (dueDate == null) {
            try {
                System.out.println("Enter the task due date (YYYY-MM-DD):");
                String dueDateStr = scanner.nextLine();
                dueDate = LocalDate.parse(dueDateStr);  // Parsing the date
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        // Call the TaskManager to add the task
        taskManager.addTask(title, dueDate, priority);
        System.out.println("Task added successfully.");
    }

    // this shows the user the viewing tasks menu.
    private void viewTask() {
        while (true) {
            // the user will choose their filtering option or choose to view all tasks
            System.out.println("\n-- View Task --");
            System.out.println("1. View all tasks");
            System.out.println("2. Filter by Date");
            System.out.println("3. Filter by priority");
            System.out.println("4. Filter by completion status");
            System.out.println("0. Back");
            System.out.println("Choose an option: ");

            String input = scanner.nextLine();
            switch (input) {
                case "1" -> {
                    // these will be sorted by date in ascending order - the soonest due date will come first.
                    List<Task> allTasks = taskManager.viewAllTasks();
                    if (allTasks.isEmpty()) {
                        System.out.println("There are no tasks in the database.");
                        return;
                    } else {
                        System.out.println("\n==== All Tasks ====");
                        printTasks(allTasks);
                    }
                }
                case "2" -> {
                    try {
                        // the user can enter a specific date to see tasks that will be due on that date
                        System.out.println("Enter date to filter by (YYYY-MM-DD):");
                        String dateInput = scanner.nextLine();
                        LocalDate dueDate = LocalDate.parse(dateInput);
                        List<Task> filteredTasks = taskManager.filterByDate(dueDate);

                        if (filteredTasks.isEmpty()) {
                            System.out.println("No tasks found for " + dueDate);
                        } else {
                            System.out.println("Tasks due on " + dueDate + ":");
                            printTasks(filteredTasks);
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid date format. Please try again using YYYY-MM-DD.");
                    }
                }
                case "3" -> {
                    // user can view tasks with a specific priority level
                    System.out.println("Enter the priority (1 to 3) to filter by:");
                    int priority = Integer.parseInt(scanner.nextLine());

                    if (priority < 1 || priority > 3) {
                        System.out.println("Invalid entry, please enter a number between 1 and 3.");
                    } else {
                        List<Task> filteredTasks = taskManager.filterByPriority(priority);
                        if (filteredTasks.isEmpty()) {
                            System.out.println("No tasks found for " + priority);
                        } else {
                            System.out.println("Tasks found for " + priority + ":");
                            printTasks(filteredTasks);
                        }
                    }
                }
                case "4" -> {
                    // user can sort tasks by completion
                    System.out.println("Enter the completion status (true for completed, false for not completed:");
                    boolean isCompleted = Boolean.parseBoolean(scanner.nextLine());
                    List<Task> filteredTasks = taskManager.filterTasksByCompletionStatus(isCompleted);

                    if (filteredTasks.isEmpty()) {
                        System.out.println("No " + isCompleted + " completed tasks found.");
                    } else {
                        System.out.println("Tasks with completion status " + isCompleted + ":");
                        printTasks(filteredTasks);
                    }
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid input");
            }
        }

    }

    // this will run when the user wants to edit a task
    private void editTask() {
        System.out.println("Enter a task ID for the task you would like to edit: ");
        String input = scanner.nextLine();
        int taskId;

        // makes sure the user entered a number and not a letter, for example
        try {
            taskId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid task ID. Please enter a valid number.");
            return;
        }


        Task taskToEdit = taskManager.getTaskById(taskId);
        if (taskToEdit == null) {
            System.out.println("Task not found. Please try again.");
            return;
        }

        System.out.println("\n-- Task Details --");
        System.out.println(taskToEdit);

        while(true) {
            System.out.println("What would you like to edit?");
            System.out.println("1. Title");
            System.out.println("2. Due date");
            System.out.println("3. Priority");
            System.out.println("4. Completion status");
            System.out.println("0. Back");
            System.out.println("Choose an option: ");

            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid entry, please try again.");
                continue;
            }

            switch (choice) {
                case 0 -> {
                    System.out.println("Returning to the previous menu.");
                    return;
                }
                case 1 -> {
                    System.out.println("Enter the new title:");
                    String newTitle = scanner.nextLine();
                    taskToEdit.setTitle(newTitle);
                    taskManager.updateTask(taskToEdit);
                }
                case 2 -> {
                    LocalDate newDueDate = null;
                    while (newDueDate == null) {
                        System.out.println("Enter the new due date (YYYY-MM-DD):");
                        try {
                            newDueDate = LocalDate.parse(scanner.nextLine());
                            taskToEdit.setDueDate(newDueDate);
                            taskManager.updateTask(taskToEdit);
                        } catch (Exception e) {
                            System.out.println("Invalid date format. Please try again.");
                        }
                    }
                    return;
                }
                case 3 -> {
                    int newPriority = 0;
                    while (newPriority < 1 || newPriority > 3) {
                        System.out.println("Enter the new priority (1 being highest, 3 being lowest):");
                        try {
                            newPriority = Integer.parseInt(scanner.nextLine());
                            if (newPriority < 1 || newPriority > 3) {
                                System.out.println("Invalid entry, please enter a number between 1 and 3.");
                            } else {
                                taskToEdit.setPriority(newPriority);
                                taskManager.updateTask(taskToEdit);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid priority. Please enter a number.");
                        }
                    }
                    return;
                }
                case 4 -> {
                    while (true) {
                        System.out.println("Has the task been completed? (true/false):");
                        String completedInput = scanner.nextLine().trim();
                        if (completedInput.equalsIgnoreCase("true") || completedInput.equalsIgnoreCase("false")) {
                            boolean newCompleted = Boolean.parseBoolean(completedInput);
                            taskToEdit.setCompleted(newCompleted);
                            taskManager.updateTask(taskToEdit);
                            System.out.println("Completion status updated.");
                            break;
                        } else {
                            System.out.println("Invalid entry, please enter 'true' or 'false'.");
                        }
                    }
                }
            }
        }
    }

    // user can delete a task by the task ID - they use the view tasks menu to find the task ID if they don't know it, this does not show the tasks
    private void deleteTask() {
        System.out.println("Enter a task ID for the task you would like to delete: ");
        String input = scanner.nextLine();
        int taskId;

        try {
            taskId = Integer.parseInt(input); // Convert the input to an integer
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID, please enter a valid number.");
            return;
        }

        Task taskToDelete = taskManager.getTaskById(taskId);
        if (taskToDelete == null) {
            System.out.println("Task with task ID " + taskId + " not found."); // Corrected message
            return;
        }

        // Show the task to delete
        System.out.println("Are you sure you want to delete the following task?");
        System.out.println(taskToDelete);
        System.out.println("Type 'yes' to confirm or 'no' to cancel: ");

        String confirmation = scanner.nextLine().trim();

        if (confirmation.equalsIgnoreCase("yes")) {
            taskManager.deleteTask(taskId); // Delete the task
            System.out.println("Task with task ID " + taskId + " deleted.");
        } else if (confirmation.equalsIgnoreCase("no")) {
            System.out.println("Task deletion cancelled.");
        } else {
            System.out.println("Invalid entry, please try again.");
        }
    }

    public void printTasks(List<Task> tasks) {
        // Print table header
        System.out.println("+-------+----------------------|------------+-----------+------------+");
        System.out.println("| TaskID| Title                | Due Date  | Priority | Completed  |");
        System.out.println("+-------+----------------------|------------+-----------+------------+");

        // Print each task using its custom toString() method
        tasks.forEach(System.out::println);

        // Print table footer
        System.out.println("+-------+----------------------|------------+-----------+------------+");
    }

}
