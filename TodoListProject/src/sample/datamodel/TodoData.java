package sample.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TodoData {

    private static TodoData instance = new TodoData();
    private Connection con;
    private Statement statement;

    private ObservableList<TodoItem> todoItems = FXCollections.observableArrayList();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    {
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/todoListItems", "root", "");
            statement = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static TodoData getInstance() {
        return instance;
    }

    private TodoData() {
    }

    public ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void addTodoItem(TodoItem item) {
        todoItems.add(item);
    }

    public void loadTodoItems() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM todo");
            while (resultSet.next()) {
                String shortDescription = resultSet.getString(2);
                String details = resultSet.getString(3);
                Date temp = resultSet.getDate(4);
                // Converting  Date to LocalDate
                LocalDate deadline = Instant.ofEpochMilli(temp.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

                TodoItem todoItem = new TodoItem(shortDescription, details, deadline);
                todoItems.add(todoItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
