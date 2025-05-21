package org.example.model;

import java.time.LocalDate;

public class Task {
    private int id;
    private String title;
    private LocalDate dueDate;
    private int priority; // e.g., 1 = High, 2 = Medium, 3 = Low
    private boolean completed;

    public Task() {
        // Default constructor
    }

    // there are two constructors since I don't need the ID to access the object every time, this way it accepts constructors either way.
    public Task(String title, LocalDate dueDate, int priority, boolean completed) {
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
    }

    public Task(int id, String title, LocalDate dueDate, int priority, boolean completed) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
    }

    // Getters and Setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public LocalDate getDueDate() { return dueDate; }

    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public int getPriority() { return priority; }

    public void setPriority(int priority) { this.priority = priority; }

    public boolean isCompleted() { return completed; }

    public void setCompleted(boolean completed) { this.completed = completed; }

    // this makes sure the printed tasks will be in the same layout to make a cleaner appearance
    @Override
    public String toString() {
        return String.format("| %-5d | %-20s | %-10s | %-10d | %-10b |",
                id, title, dueDate, priority, completed);
    }
}
