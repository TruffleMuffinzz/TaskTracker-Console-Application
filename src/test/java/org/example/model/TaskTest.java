package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

// basic unit tests for the Task.java -- found in Model

public class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task("Test Task", LocalDate.of(2025, 4, 22), 1, false);
    }

    @Test
    void testTaskConstructorAndGetters() {
        assertNotNull(task); // Ensure the task object is not null
        assertEquals("Test Task", task.getTitle()); // Verify title
        assertEquals(LocalDate.of(2025, 4, 22), task.getDueDate()); // Verify due date
        assertEquals(1, task.getPriority()); // Verify priority
        assertFalse(task.isCompleted()); // Verify completed status
    }

    @Test
    void testSetters() {
        task.setTitle("Updated Task");
        task.setDueDate(LocalDate.of(2025, 5, 1));
        task.setPriority(2);
        task.setCompleted(true);

        assertEquals("Updated Task", task.getTitle()); // Verify updated title
        assertEquals(LocalDate.of(2025, 5, 1), task.getDueDate()); // Verify updated due date
        assertEquals(2, task.getPriority()); // Verify updated priority
        assertTrue(task.isCompleted()); // Verify updated completed status
    }

    // tests to see if the toString override works as expected
    @Test
    void testToString() {
        String expectedString = "| 0     | Test Task            | 2025-04-22 | 1          | false      |"; // Corrected priority column to 1
        assertEquals(expectedString, task.toString());
    }


}
