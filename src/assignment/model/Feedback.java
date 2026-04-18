package assignment.model;

import java.time.LocalDate;

public class Feedback<T> {

    private String   courseCode;
    private String   studentName;
    private T        value;
    private String   type;   // "RATING" or "COMMENT"
    private LocalDate date;

    public Feedback(String courseCode, String studentName, T value, String type) {
        this.courseCode   = courseCode;
        this.studentName  = studentName;
        this.value        = value;
        this.type         = type;
        this.date         = LocalDate.now();
    }

    public String    getCourseCode()  { return courseCode; }
    public String    getStudentName() { return studentName; }
    public T         getValue()       { return value; }
    public String    getType()        { return type; }
    public LocalDate getDate()        { return date; }

    @Override
    public String toString() {
        return "[" + type + "] " + courseCode + " by " + studentName + ": " + value + " (" + date + ")";
    }
}