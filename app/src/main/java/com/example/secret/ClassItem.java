package com.example.secret;

import android.location.Location;

public class ClassItem {

    private String classNameAndSection;
    private String Professor;
    private String LocationAndRoomNumber;
    private String dayOfWeek; // Use Calendar.DAY_OF_WEEK values
    private String startTime; // Use Calendar.HOUR_OF_DAY values
    private String endTime;
    private int id;

    // Constructors
    public ClassItem() {
        // Default constructor
    }

    public ClassItem(String className, String professor, String locationAndRoomNumber, String dayOfWeek, String startTime, String endTime) {
        this.classNameAndSection = className;
        this.Professor = professor;
        this.LocationAndRoomNumber = locationAndRoomNumber;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and setters
    public String getClassNameAndSection() {
        return classNameAndSection;
    }

    public void setClassNameAndSection(String classNameAndSection) {
        this.classNameAndSection = classNameAndSection;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getProfessor() {return Professor;}

    public void setProfessor(String professor) {this.Professor = professor;}

    public String getLocationAndRoomNumber() {return LocationAndRoomNumber;}

    public void setLocationAndRoomNumber(String locationAndRoomNumber) {this.LocationAndRoomNumber = locationAndRoomNumber;}
}