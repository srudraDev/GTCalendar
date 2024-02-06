package com.example.secret;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter;
    private List<ClassItem> classList = new ArrayList<>();

    private CalendarViewModel viewModel;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewCalendar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        viewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        // Observe changes in the classList LiveData
        // Update your RecyclerView with the new list of classes
        viewModel.getClassList().observe(getViewLifecycleOwner(), (Observer<List<ClassItem>>) this::updateUI);

        // Initialize and set the adapter
        scheduleAdapter = new ScheduleAdapter(classList);
        recyclerView.setAdapter(scheduleAdapter);

        FloatingActionButton fabAddClass = view.findViewById(R.id.fabAddClass);
        fabAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomPopup(view);
            }
        });

        return view;
    }
    private void updateUI(List<ClassItem> classList) {
        // Update UI components based on the classList
        scheduleAdapter.setClassList(classList);
        scheduleAdapter.notifyDataSetChanged();
    }

    private void showCustomPopup(View view) {
        Log.d("CalendarFragment", "showCustomPopup called");
        // Inflate the custom popup layout
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_add_class, null);

        // Initialize views in the popup
        EditText editClassName = popupView.findViewById(R.id.editClassName);
        Spinner spinnerDaysOfWeek = popupView.findViewById(R.id.spinnerDaysOfWeek);
        NumberPicker numberPickerStartTime = popupView.findViewById(R.id.numberPickerStartTime);
        NumberPicker numberPickerEndTime = popupView.findViewById((R.id.numberPickerEndTime));
        Button btnAddClass = popupView.findViewById(R.id.btnAddClass);

        // Set up NumberPicker for time selection
        String[] displayedValues = generateTimePickerValues();
        numberPickerStartTime.setMinValue(0);
        numberPickerStartTime.setMaxValue(displayedValues.length - 1);
        numberPickerStartTime.setDisplayedValues(displayedValues);
        numberPickerStartTime.setWrapSelectorWheel(true);

        numberPickerEndTime.setMinValue(0);
        numberPickerEndTime.setMaxValue(displayedValues.length - 1);
        numberPickerEndTime.setDisplayedValues(displayedValues);
        numberPickerEndTime.setWrapSelectorWheel(true);

        // Create a PopupWindow
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        // Set up click listener for the "Add Class" button
        btnAddClass.setOnClickListener(v -> {
            // Handle the "Add Class" button click
            String className = editClassName.getText().toString();
            String selectedDay = spinnerDaysOfWeek.getSelectedItem().toString();
            String selectedStartTime = displayedValues[numberPickerStartTime.getValue()];
            String selectedEndTime = displayedValues[numberPickerEndTime.getValue()];

            Log.d("Checker", selectedStartTime);

            ClassItem newClass = new ClassItem();
            newClass.setClassName(className);
            newClass.setDayOfWeek(selectedDay);

            newClass.setStartTime(selectedStartTime);
            newClass.setEndTime(selectedEndTime);

            // Add the newClass to the classList using the ViewModel
            viewModel.addClass(newClass);

            scheduleAdapter.notifyDataSetChanged();

            // Dismiss the popup
            popupWindow.dismiss();
        });

        // Show the popup at the center of the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
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
}