package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    // Save a new ticket
    public boolean createTicket(Ticket ticket) {

        String sql = "INSERT INTO tickets " +
                "(ticket_id, employee_name, issue, priority, status, engineer, created_at, resolved_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ticket.getTicketId());
            ps.setString(2, ticket.getEmployeeName());
            ps.setString(3, ticket.getIssue());
            ps.setString(4, ticket.getPriority());
            ps.setString(5, ticket.getStatus());
            ps.setString(6, ticket.getEngineer());
            ps.setTimestamp(7, Timestamp.valueOf(ticket.getCreatedAt()));

            if (ticket.getResolvedAt() != null) {
                ps.setTimestamp(8, Timestamp.valueOf(ticket.getResolvedAt()));
            } else {
                ps.setNull(8, Types.TIMESTAMP);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error creating ticket: " + e.getMessage());
            return false;
        }
    }

    // Retrieve all tickets
    public List<Ticket> getAllTickets() {

        List<Ticket> tickets = new ArrayList<>();

        String sql = "SELECT * FROM tickets ORDER BY ticket_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tickets.add(mapTicket(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving tickets: " + e.getMessage());
        }

        return tickets;
    }

    // Search ticket by ID
    public Ticket getTicketById(int id) {

        String sql = "SELECT * FROM tickets WHERE ticket_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapTicket(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching ticket: " + e.getMessage());
        }

        return null;
    }

    // Update ticket status
    public boolean updateTicketStatus(int id, String status) {

        String sql = "UPDATE tickets SET status = ?, resolved_at = ? " +
                     "WHERE ticket_id = ? AND status <> 'Resolved'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);

            if (status.equals("Resolved")) {
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            } else {
                ps.setNull(2, Types.TIMESTAMP);
            }

            ps.setInt(3, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating ticket: " + e.getMessage());
            return false;
        }
    }

    // Convert database row into Ticket object
    private Ticket mapTicket(ResultSet rs) throws SQLException {

        Timestamp created = rs.getTimestamp("created_at");
        Timestamp resolved = rs.getTimestamp("resolved_at");

        Ticket ticket = new Ticket(
                rs.getInt("ticket_id"),
                rs.getString("employee_name"),
                rs.getString("issue"),
                rs.getString("priority"),
                rs.getString("engineer")
        );

        ticket.setCreatedAt(created.toLocalDateTime());

        if (resolved != null) {
            ticket.setResolvedAt(resolved.toLocalDateTime());
        }

        ticket.updateStatus(rs.getString("status"));

        return ticket;
    }

    // Get next ticket ID
    public int getNextTicketId() {

        String sql = "SELECT COALESCE(MAX(ticket_id), 1000) + 1 AS next_id FROM tickets";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("next_id");
            }

        } catch (SQLException e) {
            System.out.println("Error generating ticket ID: " + e.getMessage());
        }

        return 1001;
    }

    // Get ticket count by status
    public int countByStatus(String status) {

        String sql = "SELECT COUNT(*) FROM tickets WHERE status = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error generating report: " + e.getMessage());
        }

        return 0;
    }
}
