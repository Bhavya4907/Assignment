package com.cms.model;

import com.cms.model.*;
import com.cms.ui.*;
import com.cms.data.*;
import com.cms.service.*;

import java.util.ArrayList;
import java.util.List;

public class Student {
    int currentSemester = 1;

    String branch;

    List<String> completed = new ArrayList<>();
    List<String> registered = new ArrayList<>();
    public List<String> complaint = new ArrayList<>();

    int currCred = 0;

    public Student(String branch) {
        this.branch = branch;
    }
}
