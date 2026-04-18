package assignment.model;

import assignment.model.Feedback;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackManager<T> {

    private final List<Feedback<T>> feedbackList = new ArrayList<>();

    public void addFeedback(Feedback<T> feedback) {
        feedbackList.add(feedback);
    }

    // Get all feedback for a specific course
    public List<Feedback<T>> getFeedbackByCourse(String courseCode) {
        return feedbackList.stream()
                .filter(f -> f.getCourseCode().equalsIgnoreCase(courseCode))
                .collect(Collectors.toList());
    }

    // Get all feedback submitted by a specific student
    public List<Feedback<T>> getFeedbackByStudent(String studentName) {
        return feedbackList.stream()
                .filter(f -> f.getStudentName().equalsIgnoreCase(studentName))
                .collect(Collectors.toList());
    }

    public List<Feedback<T>> getAllFeedback() {
        return new ArrayList<>(feedbackList);
    }
}