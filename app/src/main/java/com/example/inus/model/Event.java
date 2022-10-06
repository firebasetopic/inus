package com.example.inus.model;

import android.util.Log;

import java.util.ArrayList;

public class Event {

    public static ArrayList<Event> eventList = new ArrayList<>();

    public static ArrayList<Event> eventForDate(String date){  // 收 date, return event of date
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventList){  // someday : selectDay
            if(event.getDate().equals(date)){
                events.add(event);
            }
        }
        return events;
    }

    String title;
    String startTime;
    String endTime;
    String location;
    String description;

    public Event(String title, String startTime, String endTime, String location, String description) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.description = description;
    }

    public Event(String title, String startTime, String endTime) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        String[] date =  startTime.split("\\s+");  // regular Expression > 切割空白： \ 跳脫  \s space + 一或多個
        String date1 = date[0];
        return date1;
    }
}
