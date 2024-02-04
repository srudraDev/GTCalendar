package com.example.secret;
public class Task {
    private String taskName;
    private String taskDetails;
    private String selectedDate;

    public Task(String taskName, String taskDetails, String selectedDate) {
        this.taskName = taskName;
        this.taskDetails = taskDetails;
        this.selectedDate = selectedDate;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDetails() {
        return taskDetails;
    }

    public String getSelectedDate() {
        return selectedDate;
    }
}