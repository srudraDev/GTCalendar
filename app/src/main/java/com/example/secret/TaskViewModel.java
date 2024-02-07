package com.example.secret;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    public void sortByDate() {
        // Sort the tasks by due date in ascending order
        Collections.sort(taskList, new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                // Compare due dates
                SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy");
                Date date1, date2;
                try {
                    date1 = dateFormat.parse(task1.getSelectedDate());
                    date2 = dateFormat.parse(task2.getSelectedDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0; // Handle parsing error
                }

                // Compare the Date objects
                return date1.compareTo(date2);
            }
        });
        taskListLiveData.setValue(taskList);
    }
}
