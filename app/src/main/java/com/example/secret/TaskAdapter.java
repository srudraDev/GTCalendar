// TaskAdapter.java
package com.example.secret;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;
    private String taskName;
    private String taskDetails;
    private Button buttonDueDate;

    private int editingPosition = -1;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.textViewTaskName.setText(task.getTaskName());
        holder.textViewTaskDetails.setText(task.getTaskDetails());
        holder.textViewSelectedDate.setText(task.getSelectedDate());
        holder.editDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the editing position
                editingPosition = holder.getAdapterPosition();

                // Show the edit/delete popup
                showEditDeletePopup(holder.getAdapterPosition(), context);
            }
        });

    }

    private void showEditDeletePopup(int position, Context context) {
        // Inflate the popup layout
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_edit_delete_task, null);

        // Create the popup window
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Set up UI components and logic for the popup
        EditText editTextName = popupView.findViewById(R.id.EditTextName);
        EditText editTextDetails = popupView.findViewById(R.id.EditTextDetails);
        buttonDueDate = popupView.findViewById(R.id.buttonDueDate);
        Button buttonEdit = popupView.findViewById(R.id.buttonEdit);
        Button buttonDelete = popupView.findViewById(R.id.buttonDelete);

        Task existingTask = TaskViewModel.getTaskList().get(position);
        editTextName.setText(existingTask.getTaskName());
        editTextDetails.setText(existingTask.getTaskDetails());
        buttonDueDate.setText(existingTask.getSelectedDate());
        // Handle Edit button click
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle editing task (you can implement a similar popup as the "Add Task" popup)
                // ...
                taskName = editTextName.getText().toString();
                taskDetails = editTextDetails.getText().toString();
                String showDate = buttonDueDate.getText().toString();

                // Perform any additional actions needed with the entered data
                Task task = new Task(taskName, taskDetails, showDate);
                if (editingPosition != -1 && editingPosition < taskList.size()) {
                    taskList.set(editingPosition, task);
                    notifyItemChanged(editingPosition);
                }
                // Reset the editing position
                editingPosition = -1;

                // Dismiss the popup
                popupWindow.dismiss();
            }
        });
        // Handle Due Date button click (You can implement a DatePickerDialog here)
        buttonDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Handle Delete button click
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle deleting task
                // ...

                // Remove the task from the list
                taskList.remove(position);
                notifyItemRemoved(position);

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
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Handle the selected date
                        // You can update the UI or save the selected date
                        String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
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

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTaskName;
        TextView textViewTaskDetails;
        TextView textViewSelectedDate;
        AppCompatButton editDeleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTaskName = itemView.findViewById(R.id.textViewTaskName);
            textViewTaskDetails = itemView.findViewById(R.id.textViewTaskDetails);
            textViewSelectedDate = itemView.findViewById(R.id.textViewSelectedDate);
            editDeleteButton = itemView.findViewById(R.id.Edit_Delete);
        }
    }
}
