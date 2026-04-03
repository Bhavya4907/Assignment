package com.cms.service;

import com.cms.model.*;
import com.cms.ui.*;
import com.cms.data.*;

import java.util.ArrayList;

public class SystemData {
    static ArrayList<Student> students = new ArrayList<>();
    public static ArrayList<Course> courses = new ArrayList<>();

    static
    {
        courses.add(new Course("AI101","Java", "John Java" , 4, 1, new ArrayList<>()));
        courses.add(new Course("AI102","DSA","Kunji" , 5 , 1 , new ArrayList<>()));
        courses.add(new Course("MA101","Math", "Iron BBC the third" , 4 , 1, new ArrayList<>()));
        courses.add(new Course("HS101","IVS", "Hameer" , 3 , 1 , new ArrayList<>()));
    }
}
