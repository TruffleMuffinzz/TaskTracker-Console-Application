package org.example.controller;

import org.example.model.Task;
import org.example.model.TaskDao;

import java.time.LocalDate;
import java.util.List;

public class TaskManager {
    private final TaskDao taskDao;

    public TaskManager(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public void addTask(String title, LocalDate dueDate, int priority) {
        // Create a Task object using the input values
        Task task = new Task(title, dueDate, priority, false);

        // Call TaskDao to insert the task into the database
        taskDao.addTask(task);
    }

    public List<Task> viewAllTasks() {
        return taskDao.getAllTasks();
    }

    public List<Task> filterByDate(LocalDate date) {
        return taskDao.filterTasksByDate(date);
    }

    public List<Task> filterByPriority(int priority) {
        return taskDao.filterTasksByPriority(priority);
    }

    public List<Task> filterTasksByCompletionStatus(boolean isCompleted) {
        return taskDao.filterTasksByCompletionStatus(isCompleted);
    }

    public Task getTaskById(int id) {
        return taskDao.filterTasksById(id);
    }

    public void updateTask(Task updatedTask) {
        taskDao.updateTask(updatedTask);
    }

    public void deleteTask(int id) {
        taskDao.deleteTask(id);
    }
}
