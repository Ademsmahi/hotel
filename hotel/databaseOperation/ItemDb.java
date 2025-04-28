package hotel.databaseOperation;

import hotel.classes.Item;
import javax.swing.JOptionPane;
import java.sql.*;

public class ItemDb {
    Connection conn = DataBaseConnection.connectTODB();
    PreparedStatement statement = null;
    ResultSet result = null;

    public void insertItem(Item item) {
        try {
            String insertItem = "INSERT INTO item(name, description, price) VALUES(?, ?, ?)";
            statement = conn.prepareStatement(insertItem);
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getDescription());
            statement.setDouble(3, item.getPrice());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully inserted a new Item");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nInsert Item Failed");
        } finally {
            closeStatement();
        }
    }

    public void updateItem(Item item) {
        try {
            String updateItem = "UPDATE item SET name = ?, description = ?, price = ? WHERE item_id = ?";
            statement = conn.prepareStatement(updateItem);
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getDescription());
            statement.setDouble(3, item.getPrice());
            statement.setInt(4, item.getItemId());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully updated Item");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nUpdate Item Failed");
        } finally {
            closeStatement();
        }
    }

    public ResultSet getItems() {
        try {
            String query = "SELECT * FROM item";
            statement = conn.prepareStatement(query);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError getting items");
        }
        return result;
    }

    public void deleteItem(int itemId) {
        try {
            String deleteQuery = "DELETE FROM item WHERE item_id = ?";
            statement = conn.prepareStatement(deleteQuery);
            statement.setInt(1, itemId);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Deleted item");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nDelete Item Failed");
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
