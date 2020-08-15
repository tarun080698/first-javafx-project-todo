package com.tarun.todoList.dataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TodoData {

    private static final TodoData instance = new TodoData();

    private static final String filename = "TodoListItems.txt";

    private ObservableList<TodoListItem> todoListItems;

    private final DateTimeFormatter formatter;

    public static TodoData getInstance() {
        return instance;
    }

    private TodoData() {
        formatter = DateTimeFormatter.ofPattern("MMM dd, yy");
    }

    public ObservableList<TodoListItem> getTodoListItems() {
        return todoListItems;
    }

    public void addTodoList(TodoListItem item) {
        todoListItems.add(item);
    }

    public void loadTodoItems() throws Exception {
        todoListItems = FXCollections.observableArrayList();
        Path path = Paths.get(filename);

        try (BufferedReader bf = Files.newBufferedReader(path)) {
            String input;
            while ((input = bf.readLine()) != null) {
                String[] itemsPieces = input.split("\t");
                String shortDesc = itemsPieces[0];
                String details = itemsPieces[1];
                String date = itemsPieces[2];

                LocalDate localDate = LocalDate.parse(date, formatter);
                TodoListItem todoItems = new TodoListItem(shortDesc, details, localDate);
                todoListItems.add(todoItems);
            }
        }
    }

    public void storeDataItems() throws Exception {
        Path p = Paths.get(filename);
        try (BufferedWriter bw = Files.newBufferedWriter(p)) {
            for (TodoListItem item : todoListItems) {
                bw.write(String.format("%s\t%s\t%s", item.getShortDesc(), item.getDetails(), item.getDate().format(formatter)));
                bw.newLine();
            }
        }
    }

    public void deleteItem(TodoListItem item){
        todoListItems.remove(item);
    }

}
