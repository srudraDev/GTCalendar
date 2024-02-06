package com.example.secret;
public class ClassItem {

    private String className;
    private String dayOfWeek; // Use Calendar.DAY_OF_WEEK values
    private String startTime; // Use Calendar.HOUR_OF_DAY values
    private String endTime;
    private int id;

    // Constructors
    public ClassItem() {
        // Default constructor
    }

    public ClassItem(String className, String dayOfWeek, String startTime, String endTime) {
        this.className = className;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and setters
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
}