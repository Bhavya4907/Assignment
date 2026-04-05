package assignment.model;

import assignment.data.*;
import assignment.model.*;
import assignment.ui.*;
import assignment.service.*;

public class Schedule {
    private String day;
    private String time;
    private String location;

    public Schedule( String day, String time, String location) {
        this.day = day;
        this.time = time;
        this.location = location;
    }

    public String toString() {
        return "Day: " + day + "\nTime: " + time + "\nRoom: " + location + "\n";
    }

    public String getDay() { return day; }
    public String getTime() { return time; }
    public String getLocation() { return location; }
}
