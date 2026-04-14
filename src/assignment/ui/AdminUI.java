package assignment.ui;

import assignment.model.*;
import assignment.service.Registration;
import assignment.service.SystemData;
import assignment.service.Upyogkarta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminUI extends JFrame {

    private JButton manageCourses, manageStudents, assignProf, complaints, out;

    public AdminUI() {

        // Load all students from DB on admin login
        new Upyogkarta().loadAllStudents();

        setTitle("Admin Panel");
        setLayout(new GridLayout(5, 1, 10, 10));

        manageCourses  = new JButton("Manage Course Catalog");
        manageStudents = new JButton("Manage Student Records & Grades");
        assignProf     = new JButton("Assign Professors to Courses");
        complaints     = new JButton("Handle Complaints");
        out            = new JButton("Logout");

        add(manageCourses);
        add(manageStudents);
        add(assignProf);
        add(complaints);
        add(out);

        setSize(420, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        manageCourses.addActionListener(e -> {

            String[] options = {"View Courses", "Add Course", "Delete Course"};
            int choice = JOptionPane.showOptionDialog(this,
                    "What would you like to do?", "Manage Course Catalog",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);

            if (choice == 0) viewCourses();
            else if (choice == 1) addCourse();
            else if (choice == 2) deleteCourse();
        });

        manageStudents.addActionListener(e -> {

            String[] options = {"View All Students", "Update Student Info", "Assign Grade"};
            int choice = JOptionPane.showOptionDialog(this,
                    "What would you like to do?", "Manage Students",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);

            if (choice == 0) viewAllStudents();
            else if (choice == 1) updateStudentInfo();
            else if (choice == 2) assignGrade();
        });

        assignProf.addActionListener(e -> assignProfessor());

        complaints.addActionListener(e -> handleComplaints());

        out.addActionListener(e -> dispose());
    }


    private void viewCourses() {
        String[] cols = {"Code", "Title", "Professor", "Credits",
                "Semester", "Prerequisites", "Schedule"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Course c : SystemData.courses) {
            String prereqs = (c.getPrerequisites() == null || c.getPrerequisites().isEmpty())
                    ? "None" : String.join(", ", c.getPrerequisites());
            Schedule s = c.getSchedule();
            String sched = (s != null)
                    ? s.getDay() + " " + s.getTime() + " @ " + s.getLocation()
                    : "Not set";

            model.addRow(new Object[]{
                    c.getCode(), c.getTitle(), c.getProfessor(),
                    c.getCredit(), c.getSemester(), prereqs, sched
            });
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No courses in catalog.");
            return;
        }
        showTable("Course Catalog", model);
    }

    private void addCourse() {
        String code = JOptionPane.showInputDialog(this, "Course code (e.g. CS201):");
        if (code == null || code.trim().isEmpty()) return;

        // Check duplicate
        for (Course c : SystemData.courses) {
            if (c.getCode().equalsIgnoreCase(code.trim())) {
                JOptionPane.showMessageDialog(this, "Course code already exists!");
                return;
            }
        }

        String title = JOptionPane.showInputDialog(this, "Course title:");
        if (title == null || title.trim().isEmpty()) return;

        String creditStr = JOptionPane.showInputDialog(this, "Credits (2 or 4):");
        int credit;
        try {
            credit = Integer.parseInt(creditStr.trim());
            if (credit != 2 && credit != 4) throw new NumberFormatException();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Credits must be 2 or 4!");
            return;
        }

        String semStr = JOptionPane.showInputDialog(this, "Semester (1, 2, 3 ...):");
        int sem;
        try { sem = Integer.parseInt(semStr.trim()); }
        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid semester!"); return; }

        String prereqInput = JOptionPane.showInputDialog(this,
                "Prerequisites (comma-separated codes, blank for none):");
        List<String> prereqs = new ArrayList<>();
        if (prereqInput != null && !prereqInput.trim().isEmpty())
            prereqs = Arrays.asList(prereqInput.trim().split("\\s*,\\s*"));

        String prof = JOptionPane.showInputDialog(this, "Assign professor name (or leave blank):");

        Course newCourse = new Course(
                code.trim().toUpperCase(), title.trim(),
                prof != null ? prof.trim() : "TBA",
                credit, sem, prereqs);

        SystemData.courses.add(newCourse);
        JOptionPane.showMessageDialog(this,
                "Course " + code.trim().toUpperCase() + " added successfully!");
    }

    private void deleteCourse() {
        StringBuilder sb = new StringBuilder("Existing courses:\n");
        for (Course c : SystemData.courses)
            sb.append(c.getCode()).append(" – ").append(c.getTitle()).append("\n");

        String code = JOptionPane.showInputDialog(this,
                sb + "\nEnter course code to delete:");
        if (code == null || code.trim().isEmpty()) return;

        Course toRemove = null;
        for (Course c : SystemData.courses) {
            if (c.getCode().equalsIgnoreCase(code.trim())) { toRemove = c; break; }
        }

        if (toRemove == null) {
            JOptionPane.showMessageDialog(this, "Course not found!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete " + toRemove.getCode() + " – " + toRemove.getTitle() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            SystemData.courses.remove(toRemove);
            JOptionPane.showMessageDialog(this, "Course deleted.");
        }
    }


    private void viewAllStudents() {
        if (SystemData.students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students found.");
            return;
        }

        String[] cols = {"ID", "Name", "Email", "Branch",
                "Semester", "Credits", "CGPA", "Registered Courses"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Student s : SystemData.students) {
            model.addRow(new Object[]{
                    s.id, s.name,
                    s.email != null ? s.email : "N/A",
                    s.branch, s.currentSemester,
                    s.currCred + "/" + Student.MAX_CREDITS,
                    String.format("%.2f", s.calculateCGPA()),
                    String.join(", ", s.registered)
            });
        }
        showTable("All Students", model);
    }

    private void updateStudentInfo() {
        String id = JOptionPane.showInputDialog(this, "Enter student ID to update:");
        if (id == null || id.trim().isEmpty()) return;

        Student target = findStudentById(id.trim());
        if (target == null) {
            JOptionPane.showMessageDialog(this, "Student not found!");
            return;
        }

        String newName = inputWithDefault(this, "Name:", target.name);
        if (newName != null && !newName.trim().isEmpty()) target.name = newName.trim();

        String newBranch = inputWithDefault(this, "Branch:", target.branch);
        if (newBranch != null && !newBranch.trim().isEmpty()) target.branch = newBranch.trim();

        String newEmail = inputWithDefault(this, "Email:",
                target.email != null ? target.email : "");
        if (newEmail != null && !newEmail.trim().isEmpty()) target.email = newEmail.trim();

        JOptionPane.showMessageDialog(this, "Student record updated!");
    }

    private void assignGrade() {
        // Show all students for reference
        StringBuilder sb = new StringBuilder("Students:\n");
        for (Student s : SystemData.students)
            sb.append(s.id).append(" – ").append(s.name).append("\n");

        String id = JOptionPane.showInputDialog(this, sb + "\nEnter student ID:");
        if (id == null || id.trim().isEmpty()) return;

        Student target = findStudentById(id.trim());
        if (target == null) {
            JOptionPane.showMessageDialog(this, "Student not found!");
            return;
        }

        if (target.registered.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    target.name + " has no registered courses.");
            return;
        }

        // Show registered courses
        StringBuilder courses = new StringBuilder("Registered courses for " + target.name + ":\n");
        for (String code : target.registered) courses.append(code).append("\n");

        String courseCode = JOptionPane.showInputDialog(this,
                courses + "\nEnter course code to assign grade:");
        if (courseCode == null || courseCode.trim().isEmpty()) return;

        if (!target.registered.contains(courseCode.trim().toUpperCase())
                && target.registered.stream().noneMatch(r -> r.equalsIgnoreCase(courseCode.trim()))) {
            JOptionPane.showMessageDialog(this,
                    "Student is not registered for " + courseCode.trim());
            return;
        }

        String grade = JOptionPane.showInputDialog(this,
                "Enter grade for " + courseCode.trim().toUpperCase()
                        + " (O / A+ / A / B+ / B / C / D / F):");
        if (grade == null || grade.trim().isEmpty()) return;

        target.addGrade(courseCode.trim().toUpperCase(), grade.trim().toUpperCase());

        // Remove from DB registrations since course is now complete
        Registration.deleteRegistration(target.dbId, courseCode.trim().toUpperCase());

        JOptionPane.showMessageDialog(this,
                "Grade " + grade.trim().toUpperCase()
                        + " assigned to " + target.name
                        + " for " + courseCode.trim().toUpperCase());

        // Advance semester if all grades are in
        if (target.isSemesterComplete()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    target.name + " has no more registered courses.\n"
                            + "Advance to semester " + (target.currentSemester + 1) + "?",
                    "Advance Semester", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                target.nextSemester();
                JOptionPane.showMessageDialog(this,
                        target.name + " advanced to semester " + target.currentSemester);
            }
        }
    }


    private void assignProfessor() {
        // Show unassigned or all courses
        StringBuilder sb = new StringBuilder("Courses:\n");
        for (Course c : SystemData.courses)
            sb.append(c.getCode()).append(" – ").append(c.getTitle())
                    .append(" | Prof: ").append(c.getProfessor()).append("\n");

        String code = JOptionPane.showInputDialog(this, sb + "\nEnter course code:");
        if (code == null || code.trim().isEmpty()) return;

        Course selected = null;
        for (Course c : SystemData.courses) {
            if (c.getCode().equalsIgnoreCase(code.trim())) { selected = c; break; }
        }

        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Course not found!");
            return;
        }

        String profName = JOptionPane.showInputDialog(this,
                "Current professor: " + selected.getProfessor()
                        + "\nEnter new professor name:");
        if (profName == null || profName.trim().isEmpty()) return;

        selected.setProfessor(profName.trim());
        JOptionPane.showMessageDialog(this,
                profName.trim() + " assigned to " + selected.getCode()
                        + " – " + selected.getTitle());
    }

    private void handleComplaints() {
        if (SystemData.allComplaints.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No complaints submitted yet.");
            return;
        }

        // Filter options
        String[] filters = {"All", "Pending Only", "Resolved Only"};
        String filter = (String) JOptionPane.showInputDialog(this,
                "Filter complaints by status:",
                "Filter", JOptionPane.PLAIN_MESSAGE,
                null, filters, filters[0]);
        if (filter == null) return;

        String[] cols = {"#", "Description", "Status", "Date"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        List<Integer> shownIndices = new ArrayList<>();

        for (int i = 0; i < SystemData.allComplaints.size(); i++) {
            Complaint c = SystemData.allComplaints.get(i);
            boolean show = filter.equals("All")
                    || (filter.equals("Pending Only")  && c.getStatus().equals("Pending"))
                    || (filter.equals("Resolved Only") && c.getStatus().equals("Resolved"));

            if (show) {
                model.addRow(new Object[]{
                        i,
                        c.getDescription(),
                        c.getStatus(),
                        c.getDate() != null ? c.getDate().toString() : "N/A"
                });
                shownIndices.add(i);
            }
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No complaints match the filter.");
            return;
        }

        showTable("Complaints – " + filter, model);

        // Allow resolving a complaint
        String[] resolveOptions = {"Resolve a Complaint", "Close"};
        int choice = JOptionPane.showOptionDialog(this,
                "Would you like to resolve a complaint?",
                "Complaints", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, resolveOptions, resolveOptions[1]);

        if (choice != 0) return;

        String input = JOptionPane.showInputDialog(this,
                "Enter complaint # to resolve:");
        if (input == null || input.trim().isEmpty()) return;

        try {
            int index = Integer.parseInt(input.trim());
            Complaint c = SystemData.allComplaints.get(index);

            if (c.getStatus().equals("Resolved")) {
                JOptionPane.showMessageDialog(this, "Already resolved.");
                return;
            }

            String resolution = JOptionPane.showInputDialog(this,
                    "Enter resolution details:");
            if (resolution == null || resolution.trim().isEmpty()) return;

            c.setStatus("Resolved");
            c.setResolution(resolution.trim());

            JOptionPane.showMessageDialog(this,
                    "Complaint #" + index + " marked as Resolved.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid complaint number!");
        }
    }


    private Student findStudentById(String id) {
        for (Student s : SystemData.students)
            if (s.id.equals(id)) return s;
        return null;
    }

    private String inputWithDefault(Component parent, String message, String def) {
        return (String) JOptionPane.showInputDialog(parent, message,
                "Update", JOptionPane.PLAIN_MESSAGE, null, null, def);
    }

    private void showTable(String title, DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setReorderingAllowed(false);

        JFrame f = new JFrame(title);
        f.add(new JScrollPane(table));
        f.setSize(950, 380);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}