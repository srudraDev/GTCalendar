package com.example.secret;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class TaskViewModel extends ViewModel {

    private final List<Task> taskList = new ArrayList<>();

    public List<Task> getTaskList() {
        return taskList;
    }

    public void addTask(Task task) {
        taskList.add(task);
    }
}
