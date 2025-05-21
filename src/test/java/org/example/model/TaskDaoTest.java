package org.example.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// testing the testDao (CRUD functionalities) also tests the functionality of the databasehelper and taskManager
public class TaskDaoTest {
    private DatabaseHelper databaseHelper; // Using DatabaseHelper
    private TaskDao taskDao;

    // sets up a new database connection before running each test
    @BeforeEach
    public void setUp() throws SQLException {
        databaseHelper = new DatabaseHelper(); // Initialize DatabaseHelper
        taskDao = new TaskDao(databaseHelper); // Pass DatabaseHelper to TaskDao
    }

    // Closes the database connection before running each test
    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up after each test
        try (Connection connection = databaseHelper.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS tasks");
        }
        databaseHelper.closeConnection(); // Close the connection after tests
    }

    // test to add a task
    @Test
    public void testCreateTask() throws SQLException {
        // Create a new task
        Task task = new Task("Test Task", LocalDate.of(2025, 4, 22), 1, false);
        taskDao.addTask(task); // Insert task into database

        // Ensure the task gets assigned an ID after insertion
        assertNotNull(task.getId()); // Check if the ID is set after insertion

        // Retrieve all tasks to check the total number of tasks
        List<Task> tasks = taskDao.getAllTasks();
        assertEquals(1, tasks.size()); // Check if only one task exists

        // Retrieve the task by ID to check if it was inserted correctly
        Task retrievedTask = taskDao.filterTasksById(task.getId());
        assertNotNull(retrievedTask);
        assertEquals(task.getTitle(), retrievedTask.getTitle());
        assertEquals(task.getDueDate(), retrievedTask.getDueDate());
        assertEquals(task.getPriority(), retrievedTask.getPriority());
        assertEquals(task.isCompleted(), retrievedTask.isCompleted());
    }

    // check that system allows users to view all tasks
    @Test
    public void testGetAllTasks() {
        // Add some tasks
        Task task1 = new Task("Task 1", LocalDate.of(2025, 4, 22), 1, false);
        Task task2 = new Task("Task 2", LocalDate.of(2025, 4, 23), 2, true);
        taskDao.addTask(task1);
        taskDao.addTask(task2);

        // Get all tasks
        List<Task> tasks = taskDao.getAllTasks();

        // Verify the number of tasks
        assertNotNull(tasks);
        assertEquals(2, tasks.size());

        // Verify that the tasks match what was inserted
        assertEquals("Task 1", tasks.get(0).getTitle());
        assertEquals("Task 2", tasks.get(1).getTitle());
    }

    // checks if user can successfully edit a task
    @Test
    public void testUpdateTask() throws SQLException {
        // Create a task
        Task task = new Task("Test Task", LocalDate.of(2025, 4, 22), 1, false);
        taskDao.addTask(task);

        // Update the task
        task.setTitle("Updated Task");
        task.setPriority(3);
        task.setDueDate(LocalDate.of(2025, 5, 22));
        task.setCompleted(true);
        taskDao.updateTask(task);

        // Retrieve the updated task
        Task updatedTask = taskDao.filterTasksById(task.getId());

        // Verify the changes
        assertNotNull(updatedTask);
        assertEquals("Updated Task", updatedTask.getTitle());
        assertEquals(3, updatedTask.getPriority());
        assertEquals(LocalDate.of(2025, 5, 22), updatedTask.getDueDate());
        assertTrue(updatedTask.isCompleted());
    }

    // test if user can delete a task
    @Test
    public void testDeleteTask() throws SQLException {
        // Create and add a task
        Task task = new Task("Task to Delete", LocalDate.of(2025, 4, 22), 1, false);
        taskDao.addTask(task);

        // Delete the task
        taskDao.deleteTask(task.getId());

        // Try to retrieve the task
        Task deletedTask = taskDao.filterTasksById(task.getId());

        // Verify that the task was deleted
        assertNull(deletedTask);
    }

    // filters tasks by date
    @Test
    public void testFilterTasksByDate() {
        // Add tasks
        Task task1 = new Task("Task 1", LocalDate.of(2025, 4, 22), 1, false);
        Task task2 = new Task("Task 2", LocalDate.of(2025, 4, 23), 2, true);
        taskDao.addTask(task1);
        taskDao.addTask(task2);

        // Filter tasks by date
        List<Task> tasks = taskDao.filterTasksByDate(LocalDate.of(2025, 4, 22));

        // Verify the filtered tasks
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
    }

    // test filtering ability with priority level
    @Test
    public void testFilterTasksByPriority() {
        // Add tasks with corrected priority
        Task task1 = new Task("High Priority Task", LocalDate.of(2025, 4, 22), 1, false); // High priority is 1
        Task task2 = new Task("Low Priority Task", LocalDate.of(2025, 4, 23), 3, true);  // Low priority is 3
        taskDao.addTask(task1);
        taskDao.addTask(task2);

        // Filter tasks by high priority (1)
        List<Task> highPriorityTasks = taskDao.filterTasksByPriority(1);

        // Verify the filtered tasks (should return high priority tasks)
        assertNotNull(highPriorityTasks);
        assertEquals(1, highPriorityTasks.size());
        assertEquals("High Priority Task", highPriorityTasks.get(0).getTitle());

        // Filter tasks by low priority (3)
        List<Task> lowPriorityTasks = taskDao.filterTasksByPriority(3);

        // Verify the filtered tasks (should return low priority tasks)
        assertNotNull(lowPriorityTasks);
        assertEquals(1, lowPriorityTasks.size());
        assertEquals("Low Priority Task", lowPriorityTasks.get(0).getTitle());
    }

    // test filtering by completion status
    @Test
    public void testFilterTasksByCompletionStatus() {
        // Add tasks
        Task task1 = new Task("Incomplete Task", LocalDate.of(2025, 4, 22), 1, false);
        Task task2 = new Task("Completed Task", LocalDate.of(2025, 4, 23), 2, true);
        taskDao.addTask(task1);
        taskDao.addTask(task2);

        // Filter tasks by completion status
        List<Task> tasks = taskDao.filterTasksByCompletionStatus(false);

        // Verify the filtered tasks
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Incomplete Task", tasks.get(0).getTitle());
    }
}
