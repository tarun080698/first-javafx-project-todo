package com.tarun.todoList;

import com.tarun.todoList.dataModel.TodoData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("todo.fxml"));
        primaryStage.setTitle("ToDo List");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        try {
            TodoData.getInstance().loadTodoItems();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void stop() throws Exception {
        try {
            TodoData.getInstance().storeDataItems();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
