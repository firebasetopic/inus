package com.example.inus.model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Event {

    public SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    public static ArrayList<Event> eventList = new ArrayList<>();

    public static ArrayList<Event> eventForDate(String date){  // 收 date, return event of date
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventList){  // EventDay : selectDay
            if(event.getDate().equals(date)){
                events.add(event);
            }
        }
        return events;
    }

    String title;
    Date startTime;
    Date endTime;
    String location;
    String description;

    public Event(String title, Date startTime, Date endTime, String location, String description) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.description = description;
    }

    public Event(String title, Date startTime, Date endTime) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void addInfo(Event event, String location, String description){
        this.location = location;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public Date getStartTime() { return startTime; }

    public Date getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        String date1 = SDF.format(startTime);
        return date1;
    }
}
