package com.example.secret;
public class Task {
    private String taskName;
    private String taskDetails;
    private String selectedDate;
    private boolean isAssignment;
    private boolean isExam;


    public Task(String taskName, String taskDetails, String selectedDate, boolean isAssignment, boolean isExam) {
        this.taskName = taskName;
        this.taskDetails = taskDetails;
        this.selectedDate = selectedDate;
        this.isAssignment = isAssignment;
        this.isExam = isExam;
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
    public boolean isAssignment() {
        return isAssignment;
    }

    public boolean isExam() {
        return isExam;
    }
}