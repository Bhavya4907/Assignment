package assignment.ui;

import assignment.model.*;
import assignment.service.SystemData;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ProfessorUI extends JFrame {

    Professor professor;

    JButton viewCourses, updateCourse, viewStudents;

    public ProfessorUI(Professor professor) {
        this.professor = professor;

        setTitle("Professor Panel - " + professor.name);

        viewCourses = new JButton("View My Courses");
        updateCourse = new JButton("Update Course");
        viewStudents = new JButton("View Enrolled Students");

        setLayout(new GridLayout(3, 1, 10, 10));

        add(viewCourses);
        add(updateCourse);
        add(viewStudents);

        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);


        viewCourses.addActionListener(e -> {

            StringBuilder sb = new StringBuilder();

            for (Course c : SystemData.courses) {
                if (professor.name.equalsIgnoreCase(c.getProfessor())) {
                    sb.append(c.getCode())
                            .append(" - ")
                            .append(c.toString())
                            .append("\n");
                }
            }

            JOptionPane.showMessageDialog(this, sb.toString());
        });


        updateCourse.addActionListener(e -> {

            String code = JOptionPane.showInputDialog("Enter course code:");

            Course selected = null;

            for (Course c : SystemData.courses) {
                if (c.getCode().equalsIgnoreCase(code)
                        && professor.name.equalsIgnoreCase(c.getProfessor())) {
                    selected = c;
                    break;
                }
            }

            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Course not found!");
                return;
            }

            String newTiming = JOptionPane.showInputDialog("Enter new timing:");
            String newSyllabus = JOptionPane.showInputDialog("Enter new syllabus:");

            selected.setTiming(newTiming);
            selected.setSyllabus(newSyllabus);

            JOptionPane.showMessageDialog(this, "Course updated!");
        });

        viewStudents.addActionListener(e -> {

            String code = JOptionPane.showInputDialog("Enter course code:");

            StringBuilder sb = new StringBuilder();

            for (Student s : SystemData.students) {
                if (s.registered.contains(code)) {
                    sb.append("Student (Branch: ")
                            .append(s.branch)
                            .append(")\n");
                }
            }

            JOptionPane.showMessageDialog(this,
                    sb.length() == 0 ? "No students enrolled!" : sb.toString());
        });
    }
}