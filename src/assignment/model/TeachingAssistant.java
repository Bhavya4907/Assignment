package assignment.model;

import assignment.model.Student;

public class TeachingAssistant extends Student {

    private String assignedCourse;

    public TeachingAssistant(String name, String id, String course) {
        super(name, id, course); // ✅ FIXED
        this.assignedCourse = course;
    }

    public void viewStudentGrades() {
        System.out.println("TA is viewing student grades for " + assignedCourse);
    }

    public void updateStudentGrade(String studentId, int marks) {
        System.out.println("TA updated grade of student " + studentId + " to " + marks);
    }

    public void restrictedAccess() {
        System.out.println("TAs cannot modify course details.");
    }
}