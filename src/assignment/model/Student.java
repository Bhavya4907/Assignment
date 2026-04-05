package assignment.model;

import assignment.service.SystemData;

import java.util.*;

public class Student {

    public int currentSemester = 1;
    public String branch;

    public List<String> completed = new ArrayList<>();
    public List<String> registered = new ArrayList<>();

    public List<Complaint> complaints = new ArrayList<>();


    public Map<String, String> grades = new HashMap<>();

    public int currCred = 0;
    public static final int MAX_CREDITS = 20;

    public Student(String branch) {
        this.branch = branch;
    }


    public boolean registerCourse(Course course) {

        if (course.getSemester() != currentSemester) {
            System.out.println("Only current semester allowed!");
            return false;
        }

        if (currCred + course.getCredit() > MAX_CREDITS) {
            System.out.println("Credit limit exceeded!");
            return false;
        }

        if (!completed.containsAll(course.getPrerequisites())) {
            System.out.println("Prerequisites not met!");
            return false;
        }

        if (registered.contains(course.getCode())) {
            System.out.println("Already registered!");
            return false;
        }

        registered.add(course.getCode());
        currCred += course.getCredit();

        return true;
    }


    public void dropCourse(Course course) {
        if (registered.contains(course.getCode())) {
            registered.remove(course.getCode());
            currCred -= course.getCredit();
        }
    }

    public void addComplaint(String description) {
        Complaint complaint = new Complaint(description);
        complaints.add(complaint);
        SystemData.allComplaints.add(complaint);
    }


    public void addGrade(String courseCode, String grade) {
        grades.put(courseCode, grade);
        completed.add(courseCode);
        registered.remove(courseCode); // marks course as finished
    }


    public boolean isSemesterComplete() {
        return registered.isEmpty();
    }

    public void nextSemester() {
        if (isSemesterComplete()) {
            currentSemester++;
            currCred = 0;
        } else {
            System.out.println("Semester not complete yet!");
        }
    }


    public double calculateGPA() {
        int totalCredits = 0;
        double totalPoints = 0;

        for (String courseCode : grades.keySet()) {

            int credit = 4; // simplified assumption
            double gradePoint = convertGrade(grades.get(courseCode));

            totalCredits += credit;
            totalPoints += gradePoint * credit;
        }

        if (totalCredits == 0) return 0;

        return totalPoints / totalCredits;
    }


    private double convertGrade(String grade) {
        switch (grade) {
            case "A": return 10;
            case "B": return 8;
            case "C": return 6;
            case "D": return 4;
            case "F": return 0;
            default: return 0;
        }
    }
}