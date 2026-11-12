package src;

import java.time.LocalDateTime;

class Ticket {

    private int ticketId;
    private String employeeName;
    private String issue;
    private String priority;
    private String status;
    private String engineer;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    Ticket(int id, String name, String issue,
           String priority, String engineer) {

        this.ticketId = id;
        this.employeeName = name;
        this.issue = issue;
        this.priority = priority;
        this.engineer = engineer;
        this.status = "Open";
        this.createdAt = LocalDateTime.now();
        this.resolvedAt = null;
    }

    public int getTicketId() { return ticketId; }
    public String getEmployeeName() { return employeeName; }
    public String getIssue() { return issue; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public String getEngineer() { return engineer; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public void updateStatus(String newStatus) {

        if (!newStatus.equals("Open") &&
            !newStatus.equals("In Progress") &&
            !newStatus.equals("Resolved")) {
            return;
        }

        this.status = newStatus;

        if (newStatus.equals("Resolved") && this.resolvedAt == null) {
            this.resolvedAt = LocalDateTime.now();
        } else if (!newStatus.equals("Resolved")) {
            this.resolvedAt = null;
        }
    }
}