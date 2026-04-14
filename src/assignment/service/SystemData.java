package assignment.service;

import assignment.data.*;
import assignment.model.*;
import assignment.ui.*;
import assignment.service.*;

import java.util.ArrayList;

public class SystemData {
    public static ArrayList<Student> students = new ArrayList<>();
    public static ArrayList<Course> courses = new ArrayList<>();
    public static ArrayList<Complaint> allComplaints = new ArrayList<>();

    static {
        courses.add(new Course("AI101", "Java",  "John Java",           4, 1, new ArrayList<>()));
        courses.add(new Course("AI102", "DSA",   "Kunji",               5, 1, new ArrayList<>()));
        courses.add(new Course("MA101", "Math",  "Iron BBC the third",  4, 1, new ArrayList<>()));
        courses.add(new Course("HS101", "IVS",   "Hameer",              3, 1, new ArrayList<>()));
    }


    public static Course getCourseByCode(String code) {
        if (code == null) return null;
        for (Course c : courses) {
            if (c.getCode().equalsIgnoreCase(code)) {
                return c;
            }
        }
        return null;
    }
}