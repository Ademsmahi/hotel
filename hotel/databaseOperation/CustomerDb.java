package hotel.databaseOperation;

import hotel.classes.UserInfo;
import javax.swing.JOptionPane;
import java.sql.*;

public class CustomerDb {
    private Connection conn;
    private PreparedStatement statement;
    private ResultSet result;

    public CustomerDb() {
        conn = DataBaseConnection.connectTODB();
    }

    public void insertCustomer(UserInfo user) {
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

    public void deleteCustomer(int userId) {
        try {
            String sql = "DELETE FROM userInfo WHERE user_id = ?";
            statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Deleted customer");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nDelete customer failed");
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

    private void closeStatement() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            System.err.println(ex + " >> closing Statement");
        }
    }
}
