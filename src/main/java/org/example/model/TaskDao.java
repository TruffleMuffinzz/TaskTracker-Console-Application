package org.example.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// this will run the SQL statements and interact with the database
public class TaskDao {
    private Connection conn;

    public TaskDao(DatabaseHelper dbHelper) {
        this.conn = dbHelper.getConnection();
        createTable();
    }

    private void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tasks (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "due_date TEXT NOT NULL," +
                "priority INTEGER NOT NULL," +
                "isCompleted BOOLEAN NOT NULL)";

        try (Statement statement = conn.createStatement()) {
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTask(Task task) {
        String insertTask = "INSERT INTO tasks (title, due_date, priority, isCompleted) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertTask, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDueDate().toString());
            stmt.setInt(3, task.getPriority());
            stmt.setBoolean(4, task.isCompleted());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        task.setId(generatedKeys.getInt(1));  // Set the generated ID
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String selectAllTasks = "SELECT * FROM tasks ORDER BY due_date ASC";
        try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(selectAllTasks)) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public Task filterTasksById(int id) {
        String selectSQL = "SELECT * FROM tasks WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(selectSQL)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTask(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Task> filterTasksByDate(LocalDate date) {
        List<Task> tasks = new ArrayList<>();
        String selectSQL = "SELECT * FROM tasks WHERE due_date = ?";
        try (PreparedStatement stmt = conn.prepareStatement(selectSQL)) {
            stmt.setString(1, date.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public List<Task> filterTasksByPriority(int priority) {
        List<Task> tasks = new ArrayList<>();
        String selectSQL = "SELECT * FROM tasks WHERE priority = ?";
        try (PreparedStatement stmt = conn.prepareStatement(selectSQL)) {
            stmt.setInt(1, priority);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public List<Task> filterTasksByCompletionStatus(boolean isCompleted) {
        List<Task> tasks = new ArrayList<>();
        String selectSQL = "SELECT * FROM tasks WHERE isCompleted = ?";
        try (PreparedStatement stmt = conn.prepareStatement(selectSQL)) {
            stmt.setBoolean(1, isCompleted);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public void updateTask(Task task) {
        String updateSQL = "UPDATE tasks SET title = ?, due_date = ?, priority = ?, isCompleted = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDueDate().toString());
            stmt.setInt(3, task.getPriority());
            stmt.setBoolean(4, task.isCompleted());
            stmt.setInt(5, task.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(int id) {
        String deleteSQL = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        LocalDate dueDate = LocalDate.parse(rs.getString("due_date"));
        int priority = rs.getInt("priority");
        boolean isCompleted = rs.getBoolean("isCompleted");

        return new Task(id, title, dueDate, priority, isCompleted);
    }
}
