package org.example;

import org.example.controller.TaskManager;
import org.example.model.DatabaseHelper;
import org.example.model.TaskDao;
import org.example.view.ConsoleView;

public class Main {

    public static void main(String[] args) {
        DatabaseHelper dbHelper = new DatabaseHelper();
        TaskDao taskDao = new TaskDao(dbHelper);

        TaskManager taskManager = new TaskManager(taskDao);

        ConsoleView console = new ConsoleView(taskManager, dbHelper);

        console.showMainMenu();
    }
}