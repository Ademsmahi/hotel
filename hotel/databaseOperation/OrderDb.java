package hotel.databaseOperation;

import hotel.classes.Order;
import javax.swing.JOptionPane;
import java.sql.*;

public class OrderDb {
    Connection conn = DataBaseConnection.connectTODB();
    PreparedStatement statement = null;
    ResultSet result = null;

    public void insertOrder(Order order) {
        try {
            String insertOrder = "INSERT INTO orderItem(booking_id, item_food, price, quantity, total) VALUES (?, ?, ?, ?, ?)";
            statement = conn.prepareStatement(insertOrder);
            statement.setInt(1, order.getBookingId());
            statement.setString(2, order.getFoodItem());
            statement.setDouble(3, order.getPrice());
            statement.setInt(4, order.getQuantity());
            statement.setDouble(5, order.getTotal());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully inserted a new Order");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nInsert Order Failed");
        } finally {
            closeStatement();
        }
    }

    private void closeStatement() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            System.err.println(ex.toString() + " >> Closing Statement");
        }
    }
}
