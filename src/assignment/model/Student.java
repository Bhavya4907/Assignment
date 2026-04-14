package assignment.model;

import assignment.service.SystemData;

import java.util.*;

public class Student {

    // --- Identity ---
    public String name;
    public String id;       // string id for display
    public int dbId;        // integer primary key from users table
    public String branch;
    public String email;

    // --- Semester tracking ---
    public int currentSemester = 1;

    // --- Course tracking ---
    public List<String> completed  = new ArrayList<>();
    public List<String> registered = new ArrayList<>();

    // --- Complaints ---
    public List<Complaint> complaints = new ArrayList<>();

    // --- Grades: courseCode -> grade string ---
    public Map<String, String> grades = new HashMap<>();

    // --- Credit tracking ---
    public int currCred = 0;
    public static final int MAX_CREDITS = 20;

    // --- Per-semester GPA history for CGPA ---
    public Map<Integer, Double> semesterSGPA = new LinkedHashMap<>();

    public Student(String name, String id, String branch) {
        this.name   = name;
        this.id     = id;
        this.branch = branch;
    }

    public boolean registerCourse(Course course) {
        if (course.getSemester() != currentSemester) return false;
        if (registered.contains(course.getCode())) return false;
        if (currCred + course.getCredit() > MAX_CREDITS) return false;

        List<String> prereqs = course.getPrerequisites();
        if (prereqs != null && !completed.containsAll(prereqs)) return false;

        registered.add(course.getCode());
        currCred += course.getCredit();
        return true;
    }

    public boolean dropCourse(Course course) {
        if (!registered.contains(course.getCode())) return false;
        registered.remove(course.getCode());
        currCred -= course.getCredit();
        return true;
    }

    public void addComplaint(String description) {
        Complaint complaint = new Complaint(description);
        complaints.add(complaint);
        SystemData.allComplaints.add(complaint);
    }

    public void addGrade(String courseCode, String grade) {
        grades.put(courseCode, grade);
        completed.add(courseCode);
        registered.remove(courseCode);
    }

    public boolean isSemesterComplete() { return registered.isEmpty(); }

    public void nextSemester() {
        if (!isSemesterComplete()) return;
        semesterSGPA.put(currentSemester, calculateSGPA());
        currentSemester++;
        currCred = 0;
    }

    public double calculateSGPA() {
        int totalCredits = 0; double totalPoints = 0;
        for (String code : grades.keySet()) {
            Course c = SystemData.getCourseByCode(code);
            if (c == null || c.getSemester() != currentSemester) continue;
            totalCredits += c.getCredit();
            totalPoints  += convertGrade(grades.get(code)) * c.getCredit();
        }
        return (totalCredits == 0) ? 0.0 : totalPoints / totalCredits;
    }

    public double calculateCGPA() {
        if (semesterSGPA.isEmpty()) return calculateOverallGPA();
        double sum = 0;
        for (double s : semesterSGPA.values()) sum += s;
        return sum / semesterSGPA.size();
    }

    public double calculateOverallGPA() {
        int totalCredits = 0; double totalPoints = 0;
        for (String code : grades.keySet()) {
            Course c = SystemData.getCourseByCode(code);
            int credit = (c != null) ? c.getCredit() : 4;
            totalCredits += credit;
            totalPoints  += convertGrade(grades.get(code)) * credit;
        }
        return (totalCredits == 0) ? 0.0 : totalPoints / totalCredits;
    }

    private double convertGrade(String grade) {
        if (grade == null) return 0;
        switch (grade.toUpperCase()) {
            case "O": case "A+": return 10;
            case "A":            return 9;
            case "B+":           return 8;
            case "B":            return 7;
            case "C":            return 6;
            case "D":            return 5;
            case "F":            return 0;
            default:             return 0;
        }
    }

    @Override
    public String toString() {
        return id + " - " + name + " | Branch: " + branch + " | Semester: " + currentSemester;
    }
}