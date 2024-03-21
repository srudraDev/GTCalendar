// TaskAdapter.java
package com.example.secret;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;
    private ConfirmationListener listener;
    private interface ConfirmationListener {
        void onConfirmationResult(boolean confirmed);
    }
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
        if (task.isAssignment()) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.assignmentColor));
        } else if (task.isExam()) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.examColor));
        } else {
            // Default color if no type is specified
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.taskColor));
        }
        holder.editDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the editing position
                editingPosition = holder.getAdapterPosition();

                // Show the edit/delete popup
                showEditDeletePopup(holder.getAdapterPosition(), context);
            }
        });

        holder.checkBoxTaskCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.checkBoxTaskCompleted.setButtonTintList(ColorStateList.valueOf(Color.BLACK));

                    // Delete the task when the checkbox is checked
                    moveTaskToTop(holder.getAdapterPosition());
                }
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
                showConfirmationDialog("edit", context);
                listener = new TaskAdapter.ConfirmationListener() {
                    public void onConfirmationResult(boolean confirmed) {
                        if (confirmed) {
                            editButtonActivate(editTextName, editTextDetails);
                        }
                    }
                };
                // Dismiss the popup
                popupWindow.dismiss();
            }
        });
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
                showConfirmationDialog("delete", context);
                listener = new TaskAdapter.ConfirmationListener() {
                    public void onConfirmationResult(boolean confirmed) {
                        if (confirmed) {
                            deleteButtonActivate(position);
                        }
                    }
                };
                // Dismiss the popup
                popupWindow.dismiss();
            }
        });

        // Show the popup at the center of the screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }
    private void editButtonActivate(EditText editTextName, EditText editTextDetails) {
        // Handle editing task (you can implement a similar popup as the "Add Task" popup)
        // ...
        taskName = editTextName.getText().toString();
        taskDetails = editTextDetails.getText().toString();
        String showDate = buttonDueDate.getText().toString();

        // Perform any additional actions needed with the entered data
        Task task = new Task(taskName, taskDetails, showDate, false, false);
        if (editingPosition != -1 && editingPosition < taskList.size()) {
            taskList.set(editingPosition, task);
            notifyItemChanged(editingPosition);
        }
        // Handle Due Date button click (You can implement a DatePickerDialog here)
        // Reset the editing position
        editingPosition = -1;
    }
    private void deleteButtonActivate(int position) {
        // Handle deleting task
        // ...

        // Remove the task from the list
        taskList.remove(position);
        notifyItemRemoved(position);
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
    public void sortByDate() {
        // Sort the tasks by due date in ascending order
        Collections.sort(taskList, new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                // Compare due dates
                return task1.getSelectedDate().compareTo(task2.getSelectedDate());
            }
        });
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
        AppCompatCheckBox checkBoxTaskCompleted;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTaskName = itemView.findViewById(R.id.textViewTaskName);
            textViewTaskDetails = itemView.findViewById(R.id.textViewTaskDetails);
            textViewSelectedDate = itemView.findViewById(R.id.textViewSelectedDate);
            editDeleteButton = itemView.findViewById(R.id.Edit_Delete);
            checkBoxTaskCompleted = itemView.findViewById(R.id.checkBoxTaskCompleted);
        }
    }
    private void moveTaskToTop(int position) {
        if (position > 0) {
            Task completedTask = taskList.get(position);
            taskList.remove(position);
            taskList.add(0, completedTask);
            notifyItemMoved(position, 0);
        }
    }
    private void showConfirmationDialog(String action, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String message = "Are you sure you want to " + action + " this item?";
        builder.setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User confirmed, set result to true
                        listener.onConfirmationResult(true);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User canceled, set result to false
                        listener.onConfirmationResult(false);
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
