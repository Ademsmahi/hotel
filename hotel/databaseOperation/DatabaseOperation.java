package hotel.databaseOperation;

import hotel.classes.UserInfo;
import javax.swing.JOptionPane;
import java.sql.*;

public class DatabaseOperation {
    private Connection conn;
    private PreparedStatement statement;
    private ResultSet result;

    public DatabaseOperation() {
        conn = DataBaseConnection.connectTODB();
    }

    public void insertCustomer(UserInfo user) throws SQLException {
        try {
            String sql = "INSERT INTO userInfo(name, address, phone, type) VALUES (?, ?, ?, ?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setString(2, user.getAddress());
            statement.setString(3, user.getPhoneNo());
            statement.setString(4, user.getType());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully inserted new customer");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nInsert customer failed");
            throw ex;
        } finally {
            closeStatement();
        }
    }

    public void updateCustomer(UserInfo user) {
        try {
            String sql = "UPDATE userInfo SET name = ?, address = ?, phone = ?, type = ? WHERE user_id = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setString(2, user.getAddress());
            statement.setString(3, user.getPhoneNo());
            statement.setString(4, user.getType());
            statement.setInt(5, user.getCustomerId());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully updated customer");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nUpdate customer failed");
        } finally {
            closeStatement();
        }
    }

    public void deleteCustomer(int userId) throws SQLException {
        try {
            String sql = "DELETE FROM userInfo WHERE user_id = ?";
            statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Deleted customer");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nDelete customer failed");
            throw ex;
        } finally {
            closeStatement();
        }
    }

    public ResultSet getAllCustomer() {
        try {
            String sql = "SELECT * FROM userInfo";
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nError retrieving customers");
        }
        return result;
    }

    public ResultSet searchUser(String user) {
        try {
            String sql = "SELECT user_id, name, address FROM userInfo WHERE name LIKE ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, "%" + user + "%");
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nSearch user failed");
        }
        return result;
    }

    public ResultSet searchAnUser(int id) {
        try {
            String sql = "SELECT * FROM userInfo WHERE user_id = ?";
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nSearch user by ID failed");
        }
        return result;
    }

    public ResultSet getAvailableRooms(long checkInTime) {
        try {
            String sql = """
                SELECT room_no
                FROM room
                  LEFT JOIN booking ON room.room_no = booking.booking_room
                WHERE booking.booking_room IS NULL
                   OR ? < booking.check_in
                   OR booking.check_out < ?
                GROUP BY room.room_no
                ORDER BY room_no
                """;
            statement = conn.prepareStatement(sql);
            statement.setLong(1, checkInTime);
            statement.setLong(2, checkInTime);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nGet available rooms failed");
        }
        return result;
    }

    public ResultSet getBookingInfo(long startDate, long endDate, String roomNo) {
        try {
            String sql = """
                SELECT *
                FROM booking
                WHERE booking_room = ?
                  AND (
                       (check_in <= ? AND (check_out = 0 OR check_out <= ?))
                       OR (check_in > ? AND check_out < ?)
                       OR (check_in <= ? AND (check_out = 0 OR check_out > ?))
                      )
                """;
            statement = conn.prepareStatement(sql);
            statement.setString(1, roomNo);
            statement.setLong(2, startDate);
            statement.setLong(3, endDate);
            statement.setLong(4, startDate);
            statement.setLong(5, endDate);
            statement.setLong(6, endDate);
            statement.setLong(7, endDate);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nGet booking info failed");
        }
        return result;
    }

    public int getCustomerId(UserInfo user) {
        int id = -1;
        try {
            String sql = "SELECT user_id FROM userInfo WHERE name = ? AND phone = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setString(2, user.getPhoneNo());
            result = statement.executeQuery();
            if (result.next()) {
                id = result.getInt("user_id");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nGet customer ID failed");
        } finally {
            closeResultAndStatement();
        }
        return id;
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
