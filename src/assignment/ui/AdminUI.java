package assignment.ui;

import assignment.model.*;
import assignment.service.SystemData;

import javax.swing.*;
import java.awt.*;

public class AdminUI extends JFrame {

    JButton manageCourses, manageStudents, assignProf, complaints;

    public AdminUI() {

        setTitle("Admin Panel");

        setLayout(new GridLayout(4, 1, 10, 10));

        manageCourses = new JButton("Manage Courses");
        manageStudents = new JButton("Manage Students / Assign Grades");
        assignProf = new JButton("Assign Professors");
        complaints = new JButton("Handle Complaints");

        add(manageCourses);
        add(manageStudents);
        add(assignProf);
        add(complaints);

        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        manageCourses.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Course c : SystemData.courses) {
                sb.append(c).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });


        manageStudents.addActionListener(e -> {

            String email = JOptionPane.showInputDialog("Enter student email:");

            Student student = null;

            for (Student s : SystemData.students) {
                // You may need to store email in Student later
                student = s; // simplified
                break;
            }

            if (student == null) {
                JOptionPane.showMessageDialog(this, "Student not found!");
                return;
            }

            String courseCode = JOptionPane.showInputDialog("Enter course code:");
            String grade = JOptionPane.showInputDialog("Enter grade (A/B/C/D/F):");

            student.addGrade(courseCode, grade);

            JOptionPane.showMessageDialog(this, "Grade assigned!");

            // move semester if complete
            student.nextSemester();
        });


        assignProf.addActionListener(e -> {

            String code = JOptionPane.showInputDialog("Enter course code:");
            String prof = JOptionPane.showInputDialog("Enter professor name:");

            for (Course c : SystemData.courses) {
                if (c.getCode().equalsIgnoreCase(code)) {
                    c.setProfessor(prof);
                    JOptionPane.showMessageDialog(this, "Professor assigned!");
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "Course not found!");
        });

        complaints.addActionListener(e -> {

            if (SystemData.allComplaints.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No complaints!");
                return;
            }

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < SystemData.allComplaints.size(); i++) {
                sb.append(i).append(": ")
                        .append(SystemData.allComplaints.get(i))
                        .append("\n");
            }

            String input = JOptionPane.showInputDialog(
                    this,
                    "Complaints:\n" + sb + "\nEnter index to resolve:"
            );

            try {
                int index = Integer.parseInt(input);

                Complaint c = SystemData.allComplaints.get(index);
                c.setStatus("Resolved");

                JOptionPane.showMessageDialog(this, "Marked as resolved!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        });
    }
}