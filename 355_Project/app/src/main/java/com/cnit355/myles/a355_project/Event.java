package com.cnit355.myles.a355_project;

/**
 * Created by jkwak95 on 11/29/2017.
 */

public class Event {
    private String title;
    private String description;
    private String location;
    private String month;
    private int year;
    private int day;

    public Event(){
        title = "";
        description = "";
        location = "";
        month= "";
        year = 0;
        day = 0;
    }

    public Event(String t, String desc, String loc, String mo, int yr, int d){
        title = t;
        description = desc;
        location = loc;
        month = mo;
        year = yr;
        day = d;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
