package com.example.secret;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TimeSlot implements GridObject, Parcelable {
    private String dayOfWeek;
    private String time;
    private String displayName;
    private String instructorName;
    private String details;

    // Constructor, getters and setters
    public TimeSlot(String dayOfWeek, String time, String courseName, String instructorName, String details) {
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.displayName = courseName;
        this.instructorName = instructorName;
        this.details = details;
    }
    public TimeSlot(String courseName, String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        this.time = "12:00 AM";
        this.displayName = courseName;
        this.instructorName = "None";
        this.details = "None";
    }

    protected TimeSlot(Parcel in) {
        dayOfWeek = in.readString();
        time = in.readString();
        displayName = in.readString();
        instructorName = in.readString();
        details = in.readString();
    }

    public static final Creator<TimeSlot> CREATOR = new Creator<TimeSlot>() {
        @Override
        public TimeSlot createFromParcel(Parcel in) {
            return new TimeSlot(in);
        }

        @Override
        public TimeSlot[] newArray(int size) {
            return new TimeSlot[size];
        }
    };

    // Getters
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getTime() {
        return time;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String courseName) {
        this.displayName = courseName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    // Setters
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(dayOfWeek);
        dest.writeString(displayName);
        dest.writeString(time);
        dest.writeString(instructorName);
        dest.writeString(details);
    }
}
