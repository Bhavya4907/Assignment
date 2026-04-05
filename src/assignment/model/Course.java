package assignment.model;

import java.util.List;

import assignment.data.*;
import assignment.model.*;
import assignment.ui.*;
import assignment.service.*;

public class Course
{
    private String code;
    private String title;
    private String prof;
    private int credit;
    private int semester;
    private String professor;
    private String timing;
    private String syllabus;

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
    public String getProfessor() { return professor; }
    public void setProfessor(String professor) { this.professor = professor; }

    public String getTiming() { return timing; }
    public void setTiming(String timing) { this.timing = timing; }

    public String getSyllabus() { return syllabus; }
    public void setSyllabus(String syllabus) { this.syllabus = syllabus; }

    public String toString() {
        return code + " - " + title + " (" + credit + " credits)";
    }
}
