package assignment.data;

import assignment.data.*;
import assignment.model.*;
import assignment.ui.*;
import assignment.service.*;

import java.util.ArrayList;

public class loc {
    public ArrayList<Schedule> schedules = new ArrayList<>();

    {
        schedules.add(new Schedule("Monday", "10:00 - 11:00", "Room 101"));
        schedules.add(new Schedule("Tuesday", "12:00 - 1:30", "Room 202"));
        schedules.add(new Schedule("Wednesday", "9:00 - 10:00", "Room 303"));
        schedules.add(new Schedule("Thursday", "2:00 - 3:00", "Room 404"));
    }
}
