package com.tarun.todoList.dataModel;

import java.time.LocalDate;

public class TodoListItem {
    private final String shortDesc;
    private final String details;
    private final LocalDate date;

    public String getShortDesc() {
        return shortDesc;
    }

    public String getDetails() {
        return details;
    }

    public LocalDate getDate() {
        return date;
    }

    public TodoListItem(String shortDesc, String detail, LocalDate dateLocal) {
        this.shortDesc = shortDesc;
        this.details = detail;
        this.date = dateLocal;
    }
}
