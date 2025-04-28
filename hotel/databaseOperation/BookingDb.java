
package hotel.databaseOperation;

import hotel.classes.Booking;
import hotel.classes.Order;
import javax.swing.JOptionPane;
import java.sql.*;

public class BookingDb {
    private Connection conn;
    private PreparedStatement statement;
    private ResultSet result;

    public BookingDb() {
        conn = DataBaseConnection.connectTODB();
    }

    public void insertBooking(Booking booking) {
        String sql = """
            INSERT INTO booking
              (customer_id, booking_room, guests, check_in, check_out, booking_type, has_checked_out)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try {
            for (var room : booking.getRooms()) {
                statement = conn.prepareStatement(sql);
                statement.setInt(1, booking.getCustomer().getCustomerId());
                statement.setString(2, room.getRoomNo());
                statement.setInt(3, booking.getPerson());
                statement.setTimestamp(4, booking.getCheckInDateTime());
                statement.setTimestamp(5, booking.getCheckOutDateTime());
                statement.setString(6, booking.getBookingType());
                statement.setBoolean(7, false);
                statement.executeUpdate();
                statement.close();
            }
            JOptionPane.showMessageDialog(null, "Successfully inserted new booking(s)");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nInsert booking failed");
        }
    }

    public ResultSet getBookingInformation() {
        try {
            String query = "SELECT * FROM booking";
            statement = conn.prepareStatement(query);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nError retrieving all bookings");
        }
        return result;
    }

    public ResultSet getABooking(int bookingId) {
        try {
            String query = "SELECT * FROM booking WHERE booking_id = ?";
            statement = conn.prepareStatement(query);
            statement.setInt(1, bookingId);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nError retrieving booking by ID");
        }
        return result;
    }

    public ResultSet bookingsReadyForOrder(String roomName) {
        try {
            String query = """
                SELECT booking_id, booking_room, name
                FROM booking
                  JOIN userInfo ON booking.customer_id = userInfo.user_id
                WHERE booking_room LIKE ? AND has_checked_out = 0
                ORDER BY booking_id DESC
                """;
            statement = conn.prepareStatement(query);
            statement.setString(1, "%" + roomName + "%");
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nError retrieving bookings ready for order");
        }
        return result;
    }

    public void updateCheckOut(int bookingId, long checkOutTime) {
        try {
            String query = """
                UPDATE booking
                SET has_checked_out = ?, check_out = ?
                WHERE booking_id = ?
                """;
            statement = conn.prepareStatement(query);
            statement.setBoolean(1, true);
            statement.setLong(2, checkOutTime);
            statement.setInt(3, bookingId);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully updated check-out");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nUpdate check-out failed");
        } finally {
            closeStatement();
        }
    }

    public int getRoomPrice(int bookingId) {
        int price = -1;
        try {
            String query = """
                SELECT price
                FROM booking
                  JOIN room     ON booking_room  = room_no
                  JOIN roomType ON type          = room_class
                WHERE booking_id = ?
                """;
            statement = conn.prepareStatement(query);
            statement.setInt(1, bookingId);
            result = statement.executeQuery();
            if (result.next()) {
                price = result.getInt("price");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nError retrieving room price");
        } finally {
            closeResultAndStatement();
        }
        return price;
    }

    public void insertOrder(Order order) {
        try {
            String query = """
                INSERT INTO orderItem
                  (booking_id, item_food, price, quantity, total)
                VALUES (?, ?, ?, ?, ?)
                """;
            statement = conn.prepareStatement(query);
            statement.setInt(1, order.getBookingId());
            statement.setString(2, order.getFoodItem());
            statement.setDouble(3, order.getPrice());
            statement.setInt(4, order.getQuantity());
            statement.setDouble(5, order.getTotal());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully inserted a new order");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nInsert order failed");
        } finally {
            closeStatement();
        }
    }

    public ResultSet getAllPaymentInfo(int bookingId) {
        try {
            String query = "SELECT * FROM orderItem WHERE booking_id = ?";
            statement = conn.prepareStatement(query);
            statement.setInt(1, bookingId);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nError retrieving payment info");
        }
        return result;
    }

    private void closeStatement() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            System.err.println(ex + " >> closing Statement");
        }
    }

    private void closeResultAndStatement() {
        try {
            if (result    != null) result.close();
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            System.err.println(ex + " >> closing resources");
        }
    }
}
