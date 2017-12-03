package com.cnit355.myles.a355_project;


public class Event {
    private String title;
    private String description;
    private String location;
    private String month;
    private String year;
    private String day;
    private int id;

    public Event(){
        title = "";
        description = "";
        location = "";
        month= "";
        year = "";
        day = "";
        id = 1;
    }



    public Event(String t, String desc, String loc, String mo, String yr, String d, int i){
        title = t;
        description = desc;
        location = loc;
        month = mo;
        year = yr;
        day = d;
        id = i;

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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
