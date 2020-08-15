package com.tarun.todoList;

import com.tarun.todoList.dataModel.TodoData;
import com.tarun.todoList.dataModel.TodoListItem;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {

    @FXML
    private ListView<TodoListItem> todoListView;
    @FXML
    private TextArea itemDetails;
    @FXML
    private Label deadline;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private ToggleButton filterButton;
    private FilteredList<TodoListItem> filteredList;
    private Predicate<TodoListItem> wantAllItems;
    private Predicate<TodoListItem> wantTodayItems;

    public void initialize() {
        contextMenu = new ContextMenu();
        MenuItem menu = new MenuItem("Remove");
        menu.setOnAction(event -> {
            TodoListItem item = todoListView.getSelectionModel().getSelectedItem();
            removeItem(item);
        });

        contextMenu.getItems().addAll(menu);
        todoListView.getSelectionModel().selectedItemProperty().addListener((observableValue, todoListItem, t1) -> {
            if (t1 != null) {
                TodoListItem todoListItem1 = todoListView.getSelectionModel().getSelectedItem();
                itemDetails.setText(todoListItem1.getDetails());
                DateTimeFormatter df = DateTimeFormatter.ofPattern("MMM dd, yy");
                deadline.setText(df.format(todoListItem1.getDate()));
            }
        });

        wantAllItems = item -> true;
        wantTodayItems = item -> item.getDate().equals(LocalDate.now());
        filteredList = new FilteredList<>(TodoData.getInstance().getTodoListItems(), wantAllItems);

        SortedList<TodoListItem> sortedList = new SortedList<>(filteredList, Comparator.comparing(TodoListItem::getDate));

        todoListView.setItems(sortedList);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();

        todoListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<TodoListItem> call(ListView<TodoListItem> todoListItemListView) {
                ListCell<TodoListItem> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(TodoListItem item, boolean b) {
                        super.updateItem(item, b);
                        if (b) setText(null);
                        else {
                            setText(item.getShortDesc());
                            if (item.getDate().isBefore(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.RED);
                            } else if (item.getDate().equals(LocalDate.now().plusDays(1))) setTextFill(Color.GREEN);
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (abs, wasEmpty, isEmpty) -> {
                            if (isEmpty) cell.setContextMenu(null);
                            else cell.setContextMenu(contextMenu);
                        }
                );
                return cell;
            }
        });
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        TodoListItem todoListItem = todoListView.getSelectionModel().getSelectedItem();
        if (todoListItem != null) {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) removeItem(todoListItem);
        }
    }

    @FXML
    public void addNewItem() {
        Dialog<ButtonType> db = new Dialog<>();
        db.initOwner(mainBorderPane.getScene().getWindow());
        db.setTitle("Add new Item");
        FXMLLoader fx = new FXMLLoader();
        fx.setLocation(getClass().getResource("dialogue.fxml"));
        try {
            db.getDialogPane().setContent(fx.load());
        } catch (IOException ioe) {
            System.out.println("Dialogue error");
            ioe.printStackTrace();
        }
        db.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        db.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = db.showAndWait();
        if (ButtonType.FINISH.getButtonData() != null) {
            if (result.isPresent()) {
                DialogueController controller = fx.getController();
                TodoListItem newItem = controller.processItem();
                if (newItem != null) {
                    todoListView.getSelectionModel().select(newItem);
                } else {
                    DialogueController dc = new DialogueController();
                    dc.errorLabel.setVisible(true);
                }
            }
        }
    }

    @FXML
    public void removeItem(TodoListItem item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove an item");
        alert.setHeaderText("Wish to remove " + item.getShortDesc() + " task.");
        alert.setContentText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get().equals(ButtonType.OK))) {
            TodoData.getInstance().deleteItem(item);
        }
    }

    public void handleFilter() {
        TodoListItem item = todoListView.getSelectionModel().getSelectedItem();
        if (filterButton.isSelected()) {
            filteredList.setPredicate(wantTodayItems);
            if (filteredList.isEmpty()) {
                itemDetails.setText("No task for today. Enjoy!");
                deadline.setText("");
            } else if (filteredList.contains(item))
                todoListView.getSelectionModel().select(item);
            else todoListView.getSelectionModel().selectFirst();
        } else {
            filteredList.setPredicate(wantAllItems);
            todoListView.getSelectionModel().select(item);
        }
    }
}
