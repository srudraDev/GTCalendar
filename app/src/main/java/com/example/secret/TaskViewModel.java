package com.example.secret;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class TaskViewModel extends ViewModel {

    private MutableLiveData<List<Task>> taskListLiveData = new MutableLiveData<>();
    private static final List<Task> taskList = new ArrayList<>();

    public static List<Task> getTaskList() {
        return taskList;
    }
    public LiveData<List<Task>> getTaskListLiveData() {
        return taskListLiveData;
    }
    public void addTask(Task task) {
        taskList.add(task);
        taskListLiveData.setValue(taskList);
    }
}
