package com.tarun.todoList;

import com.tarun.todoList.dataModel.TodoData;
import com.tarun.todoList.dataModel.TodoListItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DialogueController {

    @FXML
    private TextArea details;

    @FXML
    private TextField heading;

    @FXML
    private DatePicker date;

    @FXML
    public Label errorLabel;

    public TodoListItem processItem(){
        if (heading.getText().isEmpty() || details.getText().isEmpty() || date==null){
            errorLabel.setVisible(true);
        }
        else {
            String head = heading.getText().trim();
            String detail = details.getText().trim();
            LocalDate due = date.getValue();

            TodoListItem newItem = new TodoListItem(head, detail, due);
            TodoData.getInstance().addTodoList(newItem);
            return newItem;
        }
        return null;
    }
}
