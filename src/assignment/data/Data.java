package assignment.data;

import assignment.data.*;
import assignment.model.*;
import assignment.ui.*;
import assignment.service.*;

import java.util.ArrayList;

public class Data
{
    ArrayList<Course> courses = new ArrayList<>();

    {
        courses.add(new Course("AI101","Java", "John Java" , 4, 1, new ArrayList<>()));
        courses.add(new Course("AI102","DSA","Kunji" , 5 , 1 , new ArrayList<>()));
        courses.add(new Course("MA101","Math", "Iron BBC the third" , 4 , 1, new ArrayList<>()));
        courses.add(new Course("HS101","IVS", "Hameer" , 3 , 1 , new ArrayList<>()));
    }
}
