package com.example.secret;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.secret.CalendarFragment;
import com.example.secret.R;
import com.example.secret.TimeSlot;

import java.sql.Time;
import java.util.Calendar;

public class OptionsFragment extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_params_options);

        Intent intent = getIntent();

        final TextView courseDay = (TextView) findViewById(R.id.CourseDay);
        final EditText courseName = (EditText) findViewById(R.id.CourseName);
        final EditText courseTime = (EditText) findViewById(R.id.CourseTime);
        final EditText courseDetails = (EditText) findViewById(R.id.CourseDetails);
        final EditText courseInstructor = (EditText) findViewById(R.id.CourseInstructor);
        Button saveButton = (Button) findViewById(R.id.buttonSave);
        Button deleteButton = (Button) findViewById(R.id.buttonDelete);

        if (intent != null) {
            Object event = intent.getParcelableExtra(CalendarFragment.EVENT);

            if (event instanceof TimeSlot){
                TimeSlot myEventDay = (TimeSlot) event;

                courseDay.setText(myEventDay.getDayOfWeek());
                courseName.setText(myEventDay.getDisplayName());
                courseTime.setText(myEventDay.getTime());
                courseDetails.setText(myEventDay.getDetails());
                courseInstructor.setText(myEventDay.getInstructorName());
            }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                TimeSlot myEventDay = new TimeSlot((String) courseDay.getText(), courseTime.getText().toString(), courseName.getText().toString(), courseInstructor.getText().toString(), courseDetails.getText().toString());
                returnIntent.putExtra(CalendarFragment.RETURN, myEventDay);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                TimeSlot myEventDay = new TimeSlot((String) courseDay.getText(), "12:00 AM", "None", "None", "None");
                returnIntent.putExtra(CalendarFragment.RETURN, myEventDay);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}}

