package assignment.model;

import java.time.LocalDate;

public class Complaint {

    private String description;
    private String status;
    private String resolution;
    private LocalDate date;

    public Complaint(String description) {
        this.description = description;
        this.status      = "Pending";
        this.date        = LocalDate.now();
    }

    public String getDescription()          { return description; }
    public String getStatus()               { return status; }
    public void   setStatus(String status)  { this.status = status; }
    public String getResolution()           { return resolution; }
    public void   setResolution(String r)   { this.resolution = r; }
    public LocalDate getDate()              { return date; }

    @Override
    public String toString() {
        return "[" + status + "] " + description + (resolution != null ? " → " + resolution : "");
    }
}