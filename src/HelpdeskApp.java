package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class HelpdeskApp {

    static Scanner sc = new Scanner(System.in);

    // Database access object
    static TicketDAO ticketDAO = new TicketDAO();

    static ArrayList<Engineer> engineers = new ArrayList<>();

    static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    // Detect ticket priority automatically
    static String detectPriority(String issue) {

        String text = issue.toLowerCase();

        if (text.contains("server down") ||
            text.contains("system crash") ||
            text.contains("security breach")) {
            return "Critical";
        }

        if (text.contains("network") ||
            text.contains("login") ||
            text.contains("not working")) {
            return "High";
        }

        if (text.contains("slow") ||
            text.contains("error")) {
            return "Medium";
        }

        return "Low";
    }

    // Assign engineer based on active workload
    static String assignEngineer() {

        List<Ticket> tickets = ticketDAO.getAllTickets();

        Engineer selected = null;
        int minWorkload = Integer.MAX_VALUE;

        for (Engineer engineer : engineers) {

            int workload = 0;

            for (Ticket ticket : tickets) {

                if (ticket.getEngineer().equals(engineer.getName()) &&
                    !ticket.getStatus().equals("Resolved")) {

                    workload++;
                }
            }

            if (workload < minWorkload) {
                minWorkload = workload;
                selected = engineer;
            }
        }

        return selected == null ? "Not Assigned" : selected.getName();
    }

    // Create ticket and save it in MySQL
    static void createTicket() {

        System.out.print("Enter employee name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        System.out.print("Enter issue description: ");
        String issue = sc.nextLine().trim();

        if (issue.isEmpty()) {
            System.out.println("Issue cannot be empty.");
            return;
        }

        String priority = detectPriority(issue);

        String engineer = assignEngineer();

        int ticketId = ticketDAO.getNextTicketId();

        Ticket ticket = new Ticket(
                ticketId,
                name,
                issue,
                priority,
                engineer
        );

        boolean success = ticketDAO.createTicket(ticket);

        if (success) {

            System.out.println("\nTicket created successfully!");
            System.out.println("Ticket ID: " + ticket.getTicketId());
            System.out.println("Priority: " + ticket.getPriority());
            System.out.println("Assigned Engineer: " + ticket.getEngineer());

        } else {
            System.out.println("Unable to create ticket. Please try again.");
        }
    }

    // View all tickets from MySQL
    static void viewTickets() {

        List<Ticket> tickets = ticketDAO.getAllTickets();

        if (tickets.isEmpty()) {
            System.out.println("No tickets available.");
            return;
        }

        System.out.println("\n========== ALL TICKETS ==========");

        for (Ticket ticket : tickets) {
            displayTicket(ticket);
        }
    }

    // Display ticket details
    static void displayTicket(Ticket ticket) {

        System.out.println("--------------------------------");

        System.out.println("Ticket ID: " + ticket.getTicketId());
        System.out.println("Employee: " + ticket.getEmployeeName());
        System.out.println("Issue: " + ticket.getIssue());
        System.out.println("Priority: " + ticket.getPriority());
        System.out.println("Engineer: " + ticket.getEngineer());
        System.out.println("Status: " + ticket.getStatus());

        System.out.println(
                "Created: " + ticket.getCreatedAt().format(formatter)
        );

        if (ticket.getResolvedAt() != null) {

            System.out.println(
                    "Resolved: " + ticket.getResolvedAt().format(formatter)
            );

            long minutes = Duration.between(
                    ticket.getCreatedAt(),
                    ticket.getResolvedAt()
            ).toMinutes();

            System.out.println("Resolution Time: " + minutes + " minutes");
        }
    }

    // Search ticket by ID
    static void searchTicket() {

        System.out.print("Enter ticket ID: ");

        int id = readNumber();

        Ticket ticket = ticketDAO.getTicketById(id);

        if (ticket == null) {
            System.out.println("Ticket not found.");
        } else {
            displayTicket(ticket);
        }
    }

    // Update ticket status in MySQL
    static void updateStatus() {

        System.out.print("Enter ticket ID: ");

        int id = readNumber();

        Ticket ticket = ticketDAO.getTicketById(id);

        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }

        if (ticket.getStatus().equals("Resolved")) {
            System.out.println("Ticket is already resolved.");
            return;
        }

        System.out.println("\n===== UPDATE TICKET STATUS =====");
        System.out.println("1. Open");
        System.out.println("2. In Progress");
        System.out.println("3. Resolved");

        System.out.print("Select new status: ");

        int choice = readNumber();

        String newStatus;

        switch (choice) {

            case 1:
                newStatus = "Open";
                break;

            case 2:
                newStatus = "In Progress";
                break;

            case 3:
                newStatus = "Resolved";
                break;

            default:
                System.out.println("Invalid status choice!");
                return;
        }

        boolean success = ticketDAO.updateTicketStatus(id, newStatus);

        if (success) {

            System.out.println("Ticket status updated successfully!");

            Ticket updatedTicket = ticketDAO.getTicketById(id);

            System.out.println("New Status: " + updatedTicket.getStatus());

        } else {
            System.out.println("Unable to update ticket status.");
        }
    }

    // Generate report using database records
    static void generateReport() {

        List<Ticket> tickets = ticketDAO.getAllTickets();

        int open = 0;
        int progress = 0;
        int resolved = 0;

        int critical = 0;
        int high = 0;
        int medium = 0;
        int low = 0;

        for (Ticket ticket : tickets) {

            switch (ticket.getStatus()) {

                case "Open":
                    open++;
                    break;

                case "In Progress":
                    progress++;
                    break;

                case "Resolved":
                    resolved++;
                    break;
            }

            switch (ticket.getPriority()) {

                case "Critical":
                    critical++;
                    break;

                case "High":
                    high++;
                    break;

                case "Medium":
                    medium++;
                    break;

                case "Low":
                    low++;
                    break;
            }
        }

        System.out.println("\n====== HELP DESK REPORT ======");

        System.out.println("Total Tickets: " + tickets.size());
        System.out.println("Open Tickets: " + open);
        System.out.println("In Progress: " + progress);
        System.out.println("Resolved: " + resolved);

        System.out.println("\nPriority Summary");

        System.out.println("Critical: " + critical);
        System.out.println("High: " + high);
        System.out.println("Medium: " + medium);
        System.out.println("Low: " + low);

        System.out.println("\nEngineer Workload");

        for (Engineer engineer : engineers) {

            int count = 0;

            for (Ticket ticket : tickets) {

                if (ticket.getEngineer().equals(engineer.getName()) &&
                    !ticket.getStatus().equals("Resolved")) {

                    count++;
                }
            }

            System.out.println(
                    engineer.getName() + " - Active Tickets: " + count
            );
        }
    }

    // Read integer input safely
    static int readNumber() {

        while (!sc.hasNextInt()) {

            System.out.print("Enter a valid number: ");

            sc.nextLine();
        }

        int number = sc.nextInt();

        sc.nextLine();

        return number;
    }

    // View open tickets
    static void viewOpenTickets() {

        List<Ticket> tickets = ticketDAO.getAllTickets();

        boolean found = false;

        System.out.println("\n===== OPEN TICKETS =====");

        for (Ticket ticket : tickets) {

            if (ticket.getStatus().equals("Open")) {

                displayTicket(ticket);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No open tickets available.");
        }
    }

    // View resolved tickets
    static void viewResolvedTickets() {

        List<Ticket> tickets = ticketDAO.getAllTickets();

        boolean found = false;

        System.out.println("\n===== RESOLVED TICKETS =====");

        for (Ticket ticket : tickets) {

            if (ticket.getStatus().equals("Resolved")) {

                displayTicket(ticket);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No resolved tickets available.");
        }
    }

    // Count high and critical priority tickets
    static void countHighAndCriticalTickets() {

        List<Ticket> tickets = ticketDAO.getAllTickets();

        int highCount = 0;
        int criticalCount = 0;

        for (Ticket ticket : tickets) {

            if (ticket.getPriority().equals("High")) {
                highCount++;
            }

            if (ticket.getPriority().equals("Critical")) {
                criticalCount++;
            }
        }

        System.out.println("\n===== PRIORITY SUMMARY =====");

        System.out.println("High Priority Tickets: " + highCount);
        System.out.println("Critical Priority Tickets: " + criticalCount);

        System.out.println(
                "Total High + Critical Tickets: " +
                (highCount + criticalCount)
        );
    }

    // Main method
    public static void main(String[] args) {

        engineers.add(new Engineer("Rahul"));
        engineers.add(new Engineer("Priya"));
        engineers.add(new Engineer("Arun"));

        System.out.println("Welcome to IT Helpdesk System");

        // Check database connection
        try (java.sql.Connection connection = DBConnection.getConnection()) {

            System.out.println("MySQL Database Connected Successfully!");

        } catch (Exception e) {

            System.out.println("Database connection failed!");
            System.out.println(e.getMessage());
            return;
        }

        while (true) {

            System.out.println("\n===== IT HELPDESK SYSTEM =====");

            System.out.println("1. Create Ticket");
            System.out.println("2. View All Tickets");
            System.out.println("3. Search Ticket");
            System.out.println("4. Update Ticket Status");
            System.out.println("5. Generate Report");
            System.out.println("6. View Open Tickets");
            System.out.println("7. View Resolved Tickets");
            System.out.println("8. Count High and Critical Tickets");
            System.out.println("9. Exit");

            System.out.print("Enter your choice: ");

            int choice = readNumber();

            switch (choice) {

                case 1:
                    createTicket();
                    break;

                case 2:
                    viewTickets();
                    break;

                case 3:
                    searchTicket();
                    break;

                case 4:
                    updateStatus();
                    break;

                case 5:
                    generateReport();
                    break;

                case 6:
                    viewOpenTickets();
                    break;

                case 7:
                    viewResolvedTickets();
                    break;

                case 8:
                    countHighAndCriticalTickets();
                    break;

                case 9:
                    System.out.println("Thank you for using IT Helpdesk System!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
