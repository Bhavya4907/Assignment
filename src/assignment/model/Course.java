package assignment.model;

import java.util.List;

public class Course {
    private String code;
    private String title;
    private String prof;
    private int credit;
    private int semester;
    private String professor;
    private Schedule schedule;
    private String syllabus;
    private int enrollmentLimit;
    private String officeHours;
    private List<String> prerequisites;

    public Course(String code, String title, String prof, int credit, int semester, List<String> prerequisites) {
        this.code             = code;
        this.title            = title;
        this.prof             = prof;
        this.professor        = prof;
        this.credit           = credit;
        this.semester         = semester;
        this.prerequisites    = prerequisites;
        this.enrollmentLimit  = 0;
        this.officeHours      = "Not set";
    }


    public int getSemester()               { return semester; }
    public int getCredit()                 { return credit; }
    public String getCode()                { return code; }
    public String getTitle()               { return title; }
    public List<String> getPrerequisites() { return prerequisites; }
    public String getProfessor()           { return professor; }
    public Schedule getSchedule()          { return schedule; }
    public String getSyllabus()            { return syllabus; }
    public int getEnrollmentLimit()        { return enrollmentLimit; }
    public String getOfficeHours()         { return officeHours; }


    public void setProfessor(String professor)        { this.professor = professor; }
    public void setSchedule(Schedule schedule)        { this.schedule = schedule; }
    public void setSyllabus(String syllabus)          { this.syllabus = syllabus; }
    public void setCredit(int credit)                 { this.credit = credit; }
    public void setPrerequisites(List<String> prereqs){ this.prerequisites = prereqs; }
    public void setEnrollmentLimit(int limit)         { this.enrollmentLimit = limit; }
    public void setOfficeHours(String officeHours)    { this.officeHours = officeHours; }

    @Override
    public String toString() {
        return code + " - " + title + " (" + credit + " credits)";
    }
}