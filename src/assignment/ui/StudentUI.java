package assignment.ui;

import assignment.data.*;
import assignment.model.*;
import assignment.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import assignment.service.Registration;


public class StudentUI extends JFrame {

    private final Student student;

    private JButton view, reg, schedule, drop, complaint, progress, viewComplaints, out;

    public StudentUI(Student student) {
        this.student = student;

        setTitle("Student Dashboard – " + student.name + " (Sem " + student.currentSemester + ")");
        setLayout(new GridLayout(8, 1, 10, 10));

        view            = new JButton("View Available Courses");
        schedule        = new JButton("View My Schedule");
        reg             = new JButton("Register for a Course");
        drop            = new JButton("Drop a Course");
        complaint       = new JButton("Submit Complaint");
        viewComplaints  = new JButton("View My Complaints");
        progress        = new JButton("Track Academic Progress");
        out             = new JButton("Logout");

        add(view);
        add(schedule);
        add(reg);
        add(drop);
        add(complaint);
        add(viewComplaints);
        add(progress);
        add(out);

        setSize(450, 480);
        setLocationRelativeTo(null);
        setVisible(true);

        view.addActionListener(e -> {

            String[] cols = {"Code", "Title", "Professor", "Credits", "Prerequisites", "Timings"};
            DefaultTableModel model = new DefaultTableModel(cols, 0);

            for (Course c : SystemData.courses) {
                if (c.getSemester() == student.currentSemester) {
                    String prereqs = (c.getPrerequisites() == null || c.getPrerequisites().isEmpty())
                            ? "None"
                            : String.join(", ", c.getPrerequisites());

                    String timing = (c.getSchedule() != null)
                            ? c.getSchedule().getDay() + " " + c.getSchedule().getTime()
                            + " @ " + c.getSchedule().getLocation()
                            : "TBA";

                    model.addRow(new Object[]{
                            c.getCode(), c.getTitle(), c.getProfessor(),
                            c.getCredit(), prereqs, timing
                    });
                }
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No courses found for semester " + student.currentSemester);
                return;
            }
            showTable("Available Courses – Semester " + student.currentSemester, model);
        });

        // ------------------------------------------------------------------ //
        // 2. REGISTER FOR A COURSE
        // ------------------------------------------------------------------ //
        reg.addActionListener(e -> {

            StringBuilder sb = new StringBuilder();
            for (Course c : SystemData.courses) {
                if (c.getSemester() == student.currentSemester
                        && !student.registered.contains(c.getCode())
                        && !student.completed.contains(c.getCode())) {

                    String prereqs = (c.getPrerequisites() == null || c.getPrerequisites().isEmpty())
                            ? "None" : String.join(", ", c.getPrerequisites());

                    sb.append(c.getCode())
                            .append(" | ").append(c.getTitle())
                            .append(" | ").append(c.getCredit()).append(" credits")
                            .append(" | Pre: ").append(prereqs).append("\n");
                }
            }

            if (sb.length() == 0) {
                JOptionPane.showMessageDialog(this, "No more courses available for this semester.");
                return;
            }

            String code = JOptionPane.showInputDialog(this,
                    "Current credits: " + student.currCred + "/" + Student.MAX_CREDITS
                            + "\n\nAvailable Courses:\n" + sb + "\nEnter course code to register:");

            if (code == null || code.trim().isEmpty()) return;

            Course selected = SystemData.getCourseByCode(code.trim());
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Course not found!");
                return;
            }

            boolean ok = student.registerCourse(selected);
            if (ok) Registration.saveRegistration(student.dbId, selected.getCode());
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Registered for " + selected.getCode() + " successfully!\n"
                                + "Credits used: " + student.currCred + "/" + Student.MAX_CREDITS);
            } else {
                String reason = "Registration failed.";
                if (selected.getSemester() != student.currentSemester)
                    reason = "This course is not from your current semester.";
                else if (student.currCred + selected.getCredit() > Student.MAX_CREDITS)
                    reason = "Credit limit (" + Student.MAX_CREDITS + ") would be exceeded!";
                else if (student.registered.contains(selected.getCode()))
                    reason = "You are already registered for this course.";
                else {
                    List<String> prereqs = selected.getPrerequisites();
                    if (prereqs != null && !student.completed.containsAll(prereqs)) {
                        java.util.List<String> missing = new java.util.ArrayList<>(prereqs);
                        missing.removeAll(student.completed);
                        reason = "Prerequisites not met: " + missing;
                    }
                }
                JOptionPane.showMessageDialog(this, reason);
            }
        });

        schedule.addActionListener(e -> {

            String[] cols = {"Code", "Title", "Professor", "Day", "Time", "Room"};
            DefaultTableModel model = new DefaultTableModel(cols, 0);

            for (String code : student.registered) {
                Course c = SystemData.getCourseByCode(code);
                if (c == null) continue;

                Schedule s  = c.getSchedule();
                String day      = (s != null) ? s.getDay()      : "TBA";
                String time     = (s != null) ? s.getTime()     : "TBA";
                String location = (s != null) ? s.getLocation() : "TBA";

                model.addRow(new Object[]{
                        c.getCode(), c.getTitle(), c.getProfessor(), day, time, location
                });
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "You have no registered courses yet.");
                return;
            }
            showTable("My Weekly Schedule", model);
        });

        progress.addActionListener(e -> {

            if (student.grades.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No grades recorded yet.");
                return;
            }

            String[] cols = {"Course Code", "Title", "Credits", "Grade", "Grade Points"};
            DefaultTableModel model = new DefaultTableModel(cols, 0);

            for (Map.Entry<String, String> entry : student.grades.entrySet()) {
                String courseCode = entry.getKey();
                String grade      = entry.getValue();
                Course c = SystemData.getCourseByCode(courseCode);

                String title   = (c != null) ? c.getTitle()  : "Unknown";
                int    credits = (c != null) ? c.getCredit() : 0;

                model.addRow(new Object[]{courseCode, title, credits, grade, gradeToPoint(grade)});
            }

            double sgpa = student.calculateSGPA();
            double cgpa = student.calculateCGPA();

            JPanel panel = new JPanel(new BorderLayout(5, 10));
            panel.add(new JScrollPane(buildTable(model)), BorderLayout.CENTER);

            JLabel summary = new JLabel(
                    String.format("  SGPA (current semester): %.2f    |    CGPA (overall): %.2f", sgpa, cgpa));
            summary.setFont(summary.getFont().deriveFont(Font.BOLD, 13f));
            panel.add(summary, BorderLayout.SOUTH);

            JFrame f = new JFrame("Academic Progress – " + student.name);
            f.setContentPane(panel);
            f.setSize(650, 350);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });

        drop.addActionListener(e -> {

            if (student.registered.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You have no registered courses to drop.");
                return;
            }

            StringBuilder sb = new StringBuilder("Registered courses:\n");
            for (String code : student.registered) {
                Course c = SystemData.getCourseByCode(code);
                sb.append(code);
                if (c != null) sb.append(" – ").append(c.getTitle());
                sb.append("\n");
            }

            String code = JOptionPane.showInputDialog(this, sb + "\nEnter course code to drop:");
            if (code == null || code.trim().isEmpty()) return;

            Course selected = SystemData.getCourseByCode(code.trim());
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Course not found!");
                return;
            }

            boolean ok = student.dropCourse(selected);
            if (ok) Registration.deleteRegistration(student.dbId, selected.getCode());
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        selected.getCode() + " dropped.\nCredits remaining: " + student.currCred);
            } else {
                JOptionPane.showMessageDialog(this, "You are not registered for " + selected.getCode());
            }
        });

        complaint.addActionListener(e -> {

            String desc = JOptionPane.showInputDialog(this,
                    "Describe your complaint (e.g. schedule clash, grading issue):");

            if (desc != null && !desc.trim().isEmpty()) {
                student.addComplaint(desc.trim());
                JOptionPane.showMessageDialog(this,
                        "Complaint submitted! Status: Pending\n"
                                + "You can track it via \"View My Complaints\".");
            } else {
                JOptionPane.showMessageDialog(this, "Complaint description cannot be empty.");
            }
        });

        viewComplaints.addActionListener(e -> {

            if (student.complaints.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You have no complaints on record.");
                return;
            }

            String[] cols = {"#", "Description", "Status"};
            DefaultTableModel model = new DefaultTableModel(cols, 0);

            int i = 1;
            for (Complaint c : student.complaints) {
                model.addRow(new Object[]{i++, c.getDescription(), c.getStatus()});
            }
            showTable("My Complaints", model);
        });

        out.addActionListener(e -> dispose());
    }


    private void showTable(String title, DefaultTableModel model) {
        JFrame f = new JFrame(title);
        f.add(new JScrollPane(buildTable(model)));
        f.setSize(700, 350);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private JTable buildTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }

    private double gradeToPoint(String grade) {
        if (grade == null) return 0;
        switch (grade.toUpperCase()) {
            case "O":  case "A+": return 10;
            case "A":             return 9;
            case "B+":            return 8;
            case "B":             return 7;
            case "C":             return 6;
            case "D":             return 5;
            case "F":             return 0;
            default:              return 0;
        }
    }
}