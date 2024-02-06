package com.example.secret;

import java.util.ArrayList;
import java.util.List;

//this is why we use databases, because otherwise this has to exist
public class CalendarService {
    public static List<GridObject> slots = new ArrayList<>();

    public CalendarService () {
        for (int i = 0; i < 192; i++) {
            if (i % 8 == 0) {
                slots.add(new TimeGridObject("undefined"));
            } else {
                slots.add(new TimeSlot("None", bullshit(i%8)));
            }
        }
    }

    public static String bullshit(int number) {
        switch (number) {
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            default:
                return "Sunday";
        }
    }
}
