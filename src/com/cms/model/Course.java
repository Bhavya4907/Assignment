package com.cms.model;

import java.util.List;

import com.cms.model.*;
import com.cms.ui.*;
import com.cms.data.*;
import com.cms.service.*;

public class Course
{
    private String code;
    private String title;
    private String prof;
    private int credit;
    private int semester;
    private List<String> prerequisites;

    public Course(String code, String title, String prof, int credit, int semester, List<String> prerequisites) {
        this.code = code;
        this.title = title;
        this.prof = prof;
        this.credit = credit;
        this.semester = semester;
        this.prerequisites = prerequisites;
    }

    public int getSemester() { return semester; }
    public int getCredit() { return credit; }
    public String getCode() { return code; }
    public List<String> getPrerequisites() { return prerequisites; }

    public String toString() {
        return code + " - " + title + " (" + credit + " credits)";
    }
}
