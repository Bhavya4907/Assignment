package assignment.ui;

import assignment.model.*;
import assignment.service.SystemData;
import assignment.service.Upyogkarta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ProfessorUI extends JFrame {

    private final Professor professor;

    private JButton viewCourses, updateCourse, viewStudents, out;

    public ProfessorUI(Professor professor) {
        this.professor = professor;


        new Upyogkarta().loadAllStudents();

        setTitle("Professor Panel – " + professor.name);
        setLayout(new GridLayout(4, 1, 10, 10));

        viewCourses  = new JButton("View My Courses");
        updateCourse = new JButton("Update Course Details");
        viewStudents = new JButton("View Enrolled Students");
        out          = new JButton("Logout");

        add(viewCourses);
        add(updateCourse);
        add(viewStudents);
        add(out);

        setSize(420, 320);
        setLocationRelativeTo(null);
        setVisible(true);

        viewCourses.addActionListener(e -> {

            String[] cols = {"Code", "Title", "Credits", "Semester", "Prerequisites", "Schedule", "Enrollment Limit", "Office Hours", "Syllabus"};
            DefaultTableModel model = new DefaultTableModel(cols, 0);

            for (Course c : SystemData.courses) {
                if (professor.name.equalsIgnoreCase(c.getProfessor())) {

                    String prereqs = (c.getPrerequisites() == null || c.getPrerequisites().isEmpty())
                            ? "None" : String.join(", ", c.getPrerequisites());

                    Schedule s = c.getSchedule();
                    String schedule = (s != null)
                            ? s.getDay() + " " + s.getTime() + " @ " + s.getLocation()
                            : "Not set";

                    String limit = (c.getEnrollmentLimit() == 0)
                            ? "No limit" : String.valueOf(c.getEnrollmentLimit());

                    model.addRow(new Object[]{
                            c.getCode(), c.getTitle(), c.getCredit(), c.getSemester(),
                            prereqs, schedule, limit,
                            c.getOfficeHours() != null ? c.getOfficeHours() : "Not set",
                            c.getSyllabus()    != null ? c.getSyllabus()    : "Not set"
                    });
                }
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No courses assigned to you.");
                return;
            }
            showTable("My Courses – " + professor.name, model);
        });

        updateCourse.addActionListener(e -> {

            // Build list of professor's courses for reference
            StringBuilder sb = new StringBuilder("Your courses:\n");
            for (Course c : SystemData.courses) {
                if (professor.name.equalsIgnoreCase(c.getProfessor()))
                    sb.append(c.getCode()).append(" – ").append(c.getTitle()).append("\n");
            }

            String code = JOptionPane.showInputDialog(this,
                    sb + "\nEnter course code to update:");
            if (code == null || code.trim().isEmpty()) return;

            Course selected = null;
            for (Course c : SystemData.courses) {
                if (c.getCode().equalsIgnoreCase(code.trim())
                        && professor.name.equalsIgnoreCase(c.getProfessor())) {
                    selected = c;
                    break;
                }
            }

            if (selected == null) {
                JOptionPane.showMessageDialog(this,
                        "Course not found or not assigned to you!");
                return;
            }

            Schedule cur = selected.getSchedule();
            String curDay  = (cur != null) ? cur.getDay()      : "";
            String curTime = (cur != null) ? cur.getTime()     : "";
            String curRoom = (cur != null) ? cur.getLocation() : "";

            // --- Schedule ---
            String day  = inputWithDefault(this, "Day (e.g. Monday):",   curDay);
            String time = inputWithDefault(this, "Time (e.g. 10:00 AM):", curTime);
            String room = inputWithDefault(this, "Room (e.g. Room 101):", curRoom);
            if (day != null && time != null && room != null)
                selected.setSchedule(new Schedule(day, time, room));

            // --- Credits ---
            String creditStr = inputWithDefault(this,
                    "Credits (2 or 4):", String.valueOf(selected.getCredit()));
            if (creditStr != null && !creditStr.isEmpty()) {
                try {
                    int cr = Integer.parseInt(creditStr.trim());
                    if (cr == 2 || cr == 4) selected.setCredit(cr);
                    else JOptionPane.showMessageDialog(this, "Credits must be 2 or 4. Keeping old value.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number. Keeping old value.");
                }
            }

            // --- Prerequisites ---
            String curPrereqs = (selected.getPrerequisites() == null || selected.getPrerequisites().isEmpty())
                    ? "" : String.join(", ", selected.getPrerequisites());
            String prereqInput = inputWithDefault(this,
                    "Prerequisites (comma-separated codes, blank for none):", curPrereqs);
            if (prereqInput != null) {
                if (prereqInput.trim().isEmpty()) {
                    selected.setPrerequisites(new java.util.ArrayList<>());
                } else {
                    List<String> prereqs = Arrays.asList(prereqInput.trim().split("\\s*,\\s*"));
                    selected.setPrerequisites(prereqs);
                }
            }

            // --- Enrollment Limit ---
            String curLimit = selected.getEnrollmentLimit() == 0
                    ? "" : String.valueOf(selected.getEnrollmentLimit());
            String limitStr = inputWithDefault(this,
                    "Enrollment limit (0 or blank = no limit):", curLimit);
            if (limitStr != null && !limitStr.isEmpty()) {
                try {
                    selected.setEnrollmentLimit(Integer.parseInt(limitStr.trim()));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number. Keeping old value.");
                }
            }

            // --- Office Hours ---
            String officeHours = inputWithDefault(this,
                    "Office hours (e.g. Mon/Wed 2-3 PM, Room 205):",
                    selected.getOfficeHours() != null ? selected.getOfficeHours() : "");
            if (officeHours != null) selected.setOfficeHours(officeHours);

            // --- Syllabus ---
            String syllabus = inputWithDefault(this,
                    "Syllabus:", selected.getSyllabus() != null ? selected.getSyllabus() : "");
            if (syllabus != null) selected.setSyllabus(syllabus);

            JOptionPane.showMessageDialog(this, "Course updated successfully!");
        });

        // ------------------------------------------------------------------ //
        // 3. VIEW ENROLLED STUDENTS with academic standing + contact
        // ------------------------------------------------------------------ //
        viewStudents.addActionListener(e -> {

            // Build list of this professor's courses for reference
            StringBuilder sb = new StringBuilder("Your courses:\n");
            for (Course c : SystemData.courses) {
                if (professor.name.equalsIgnoreCase(c.getProfessor()))
                    sb.append(c.getCode()).append(" – ").append(c.getTitle()).append("\n");
            }

            String code = JOptionPane.showInputDialog(this,
                    sb + "\nEnter course code to view enrolled students:");
            if (code == null || code.trim().isEmpty()) return;

            // Verify this course belongs to the professor
            boolean ownsCourse = false;
            for (Course c : SystemData.courses) {
                if (c.getCode().equalsIgnoreCase(code.trim())
                        && professor.name.equalsIgnoreCase(c.getProfessor())) {
                    ownsCourse = true;
                    break;
                }
            }
            if (!ownsCourse) {
                JOptionPane.showMessageDialog(this, "Course not found or not assigned to you!");
                return;
            }

            String[] cols = {"Name", "ID", "Email", "Branch",
                    "Semester", "Registered Credits", "CGPA"};
            DefaultTableModel model = new DefaultTableModel(cols, 0);

            for (Student s : SystemData.students) {
                if (s.registered.contains(code.trim().toUpperCase())
                        || s.registered.stream().anyMatch(r -> r.equalsIgnoreCase(code.trim()))) {

                    model.addRow(new Object[]{
                            s.name,
                            s.id,
                            s.email != null ? s.email : "N/A",
                            s.branch,
                            s.currentSemester,
                            s.currCred + "/" + Student.MAX_CREDITS,
                            String.format("%.2f", s.calculateCGPA())
                    });
                }
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No students enrolled in " + code.trim().toUpperCase());
                return;
            }
            showTable("Students Enrolled in " + code.trim().toUpperCase(), model);
        });

        // ------------------------------------------------------------------ //
        // LOGOUT
        // ------------------------------------------------------------------ //
        out.addActionListener(e -> dispose());
    }
    private String inputWithDefault(Component parent, String message, String defaultValue) {
        return (String) JOptionPane.showInputDialog(
                parent, message, "Update Course",
                JOptionPane.PLAIN_MESSAGE, null, null, defaultValue);
    }

    private void showTable(String title, DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setReorderingAllowed(false);

        JFrame f = new JFrame(title);
        f.add(new JScrollPane(table));
        f.setSize(900, 350);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}