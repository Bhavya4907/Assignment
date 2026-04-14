package assignment.service;

import assignment.model.Student;
import assignment.service.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Registration {

    public static boolean saveRegistration(int studentDbId, String courseCode) {
        String query = "INSERT IGNORE INTO registrations (student_id, course_code) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, studentDbId);
            ps.setString(2, courseCode);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean deleteRegistration(int studentDbId, String courseCode) {
        String query = "DELETE FROM registrations WHERE student_id = ? AND course_code = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, studentDbId);
            ps.setString(2, courseCode);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> loadRegistrations(int studentDbId) {
        List<String> codes = new ArrayList<>();
        String query = "SELECT course_code FROM registrations WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, studentDbId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                codes.add(rs.getString("course_code"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return codes;
    }


    public static void restoreStudentRegistrations(Student student) {
        List<String> codes = loadRegistrations(student.dbId);
        student.registered.clear();
        student.currCred = 0;

        for (String code : codes) {
            assignment.model.Course c = SystemData.getCourseByCode(code);
            if (c != null) {
                student.registered.add(code);
                student.currCred += c.getCredit();
            }
        }
    }
}