// TodoFragment.java
package com.example.secret;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class ToDoFragment extends Fragment {
    private String taskName; // Variable to store task name
    private String taskDetails; // Variable to store task details
    private TaskAdapter taskAdapter;
    private Button buttonDueDate;
    private TaskViewModel taskViewModel;

    public ToDoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        taskAdapter = new TaskAdapter(requireContext(),taskViewModel.getTaskList());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(taskAdapter);
        FloatingActionButton fabAddTask = view.findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the task add popup
                showAddTaskPopup();
            }
        });
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.getSortTrigger().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean shouldSort) {
                if (shouldSort) {
                    // Refresh the task list if sorting is triggered
                    taskAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }
    private void showAddTaskPopup() {
        // Inflate the popup layout
        View popupView = getLayoutInflater().inflate(R.layout.popup_add_task, null);

        // Create the popup window
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Set up UI components and logic for the popup
        EditText editTextName = popupView.findViewById(R.id.TextName);
        EditText editTextDetails = popupView.findViewById(R.id.TextDetails);
        buttonDueDate = popupView.findViewById(R.id.buttonDueDate);
        Button buttonAddTask = popupView.findViewById(R.id.buttonAddTask);
        Spinner spinnerTaskType = popupView.findViewById(R.id.spinnerType);

        // Handle Due Date button click (You can implement a DatePickerDialog here)
        buttonDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        // Handle Add Task button click
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the entered data
                taskName = editTextName.getText().toString();
                taskDetails = editTextDetails.getText().toString();
                String showDate = buttonDueDate.getText().toString();
                String selectedTaskType = spinnerTaskType.getSelectedItem().toString();
                boolean isAssignment = selectedTaskType.equals("Assignments");
                boolean isExam= selectedTaskType.equals("Exams");




                // Perform any additional actions needed with the entered data
                Task task = new Task(taskName, taskDetails, showDate, isAssignment, isExam);
                taskViewModel.addTask(task);
                taskAdapter.notifyDataSetChanged();

                // Dismiss the popup
                popupWindow.dismiss();
            }
        });

        // Show the popup at the center of the screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }
    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Handle the selected date
                        // You can update the UI or save the selected date
                        String selectedDate = (selectedMonth + 1) + "-" + selectedDayOfMonth+ "-" + selectedYear;
                        buttonDueDate.setText(selectedDate);
                    }
                },
                year,
                month,
                day
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }
    public void refreshList() {
        // Notify the adapter of the changes
        taskAdapter.notifyDataSetChanged();
    }
}
