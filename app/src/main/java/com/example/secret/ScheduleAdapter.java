package com.example.secret;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<ClassItem> classList;
    private CalendarViewModel viewModel;
    private Context context;
    private ConfirmationListener listener;
    private boolean condition;
    private String classNameAndSection;
    private String classProfessor;
    private String classLocation;
    private Spinner selectedDay;
    private int editingPosition = -1;

    private interface ConfirmationListener {
        void onConfirmationResult(boolean confirmed);
    }

    public ScheduleAdapter(List<ClassItem> classList, CalendarViewModel viewModel, Context context) {
        this.classList = classList;
        this.viewModel = viewModel;
        this.context = context;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create a new ViewHolder
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        return new ScheduleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        // Bind data to views in the ViewHolder
        ClassItem classItem = classList.get(position);

        holder.classNameTextView.setText(classItem.getClassNameAndSection());
        holder.dayOfWeekTextView.setText(getDayOfWeekString(classItem.getDayOfWeek()));
        holder.timeTextView.setText(String.format("%s - %s", classItem.getStartTime(), classItem.getEndTime()));

        holder.editDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editingPosition = holder.getAdapterPosition();

                showEditDeletePopup(holder.getAdapterPosition(), context);
            }
        });
    }
    private void showEditDeletePopup(int position, Context context) {
        if (context == null) {
            // Ensure context is valid
            return;
        }

        // Inflate the popup layout
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_edit_delete_class, null);

        // Create the popup window
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );
        // Set up UI components and logic for the popup
        EditText editTextName = popupView.findViewById(R.id.editClassName);
        EditText editTextProfessor = popupView.findViewById(R.id.editProfessor);
        EditText editTextLocation = popupView.findViewById(R.id.editLocation);
        selectedDay = popupView.findViewById(R.id.spinnerDaysOfWeek);
        NumberPicker startTime = popupView.findViewById(R.id.numberPickerStartTime);
        NumberPicker endTime = popupView.findViewById(R.id.numberPickerEndTime);

        Button buttonEdit = popupView.findViewById(R.id.btnEdit);
        Button buttonDelete = popupView.findViewById(R.id.btnDelete);

        ClassItem existingClass = viewModel.getClassList().getValue().get(position);
        editTextName.setText(existingClass.getClassNameAndSection());
        editTextProfessor.setText(existingClass.getProfessor());
        editTextLocation.setText(existingClass.getLocationAndRoomNumber());
        selectedDay.setPrompt(existingClass.getDayOfWeek());



        // Handle Edit button click
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog("edit", context);
                listener = new ConfirmationListener() {
                    public void onConfirmationResult(boolean confirmed) {
                        if (confirmed) {
                            editButtonActivate(editTextName, editTextProfessor, editTextLocation, startTime, endTime);
                        }
                    }
                };
                // Dismiss the popup
                popupWindow.dismiss();
            }
        });
        // Handle Delete button click
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog("delete", context);
                listener = new ConfirmationListener() {
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
    @Override
    public int getItemCount() {
        // Check if classList is null, and return 0 if it is
        return (classList != null) ? classList.size() : 0;
    }

    public void setClassList(List<ClassItem> classList) {
        this.classList = classList;
    }

    public void deleteButtonActivate(int position) {
        classList.remove(position);
        notifyItemRemoved(position);
    }

    public void editButtonActivate(EditText editTextName, EditText editTextProfessor, EditText editTextLocation, NumberPicker startTime, NumberPicker endTime) {
        //number picker initialization
        String[] displayedValues = generateTimePickerValues();
        startTime.setMinValue(0);
        startTime.setMaxValue(displayedValues.length - 1);
        startTime.setDisplayedValues(displayedValues);
        startTime.setWrapSelectorWheel(true);

        endTime.setMinValue(0);
        endTime.setMaxValue(displayedValues.length - 1);
        endTime.setDisplayedValues(displayedValues);
        endTime.setWrapSelectorWheel(true);

        classNameAndSection = editTextName.getText().toString();
        classProfessor = editTextProfessor.getText().toString();
        classLocation = editTextLocation.getText().toString();
        String showDay = selectedDay.getPrompt().toString();
        String selectedStartTime = displayedValues[startTime.getValue()];
        String selectedEndTime = displayedValues[endTime.getValue()];

        // Perform any additional actions needed with the entered data
        ClassItem Class = new ClassItem(classNameAndSection, classProfessor, classLocation, showDay, selectedStartTime, selectedEndTime);
        if (editingPosition != -1 && editingPosition < classList.size()) {
            classList.set(editingPosition, Class);
            notifyItemChanged(editingPosition);
        }
        // Reset the editing position
        editingPosition = -1;
    }

    // ViewHolder class
    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        // Declare views here
        TextView classNameTextView;
        TextView dayOfWeekTextView;
        TextView timeTextView;
        AppCompatButton editDelete;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            // Initialize views here
            classNameTextView = itemView.findViewById(R.id.classNameTextView);
            dayOfWeekTextView = itemView.findViewById(R.id.dayOfWeekTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            editDelete = itemView.findViewById(R.id.Edit_Delete);
        }
    }

    // Helper method to get the string representation of the day of the week
    private String getDayOfWeekString(String dayOfWeek) {
        return dayOfWeek;
    }

    private String[] generateTimePickerValues() {
        // Generate an array of time values from 6:00 AM to 9:00 PM in 5-minute increments
        List<String> timeValues = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);

        while (calendar.get(Calendar.HOUR_OF_DAY) <= 21 || (calendar.get(Calendar.HOUR_OF_DAY) == 21 && calendar.get(Calendar.MINUTE) == 0)) {
            String timeFormatted = SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
            timeValues.add(timeFormatted);

            calendar.add(Calendar.MINUTE, 5);
        }

        return timeValues.toArray(new String[0]);
    }
    private void setConditionTrue() {
        condition = true;
    }
    private void setConditionFalse() {
        condition = false;
    }

    private void showConfirmationDialog(String action, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String message = "Are you sure you want to " + action + " this class?";
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