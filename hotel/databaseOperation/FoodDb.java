package hotel.databaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import hotel.classes.Food;

public class FoodDb {
    
    Connection conn = DataBaseConnection.connectTODB();
    PreparedStatement statement = null;
    ResultSet result = null;
    
    public void insertFood(Food food) {
        try {
            String insertFood = "INSERT INTO food(name, price) VALUES (?, ?)";
            statement = conn.prepareStatement(insertFood);
            statement.setString(1, food.getName());
            statement.setDouble(2, food.getPrice());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully inserted a new Food");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nInsert Food Failed");
        } finally {
            flushStatementOnly();
        }
    }

    public ResultSet getFoods() {
        try {
            String query = "SELECT * FROM food";
            statement = conn.prepareStatement(query);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError retrieving foods");
        }
        return result;
    }

    public void updateFood(Food food) {
        try {
            String updateFood = "UPDATE food SET name = ?, price = ? WHERE food_id = ?";
            statement = conn.prepareStatement(updateFood);
            statement.setString(1, food.getName());
            statement.setDouble(2, food.getPrice());
            statement.setInt(3, food.getFoodId());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully updated Food");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nUpdate Food Failed");
        } finally {
            flushStatementOnly();
        }
    }

    public void deleteFood(int foodId) {
        try {
            String deleteQuery = "DELETE FROM food WHERE food_id = ?";
            statement = conn.prepareStatement(deleteQuery);
            statement.setInt(1, foodId);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Deleted food");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nDelete Food Failed");
        } finally {
            flushStatementOnly();
        }
    }
    
    public void flushAll() {
        try {
            if (statement != null) statement.close();
            if (result != null) result.close();
        } catch (SQLException ex) {
            System.err.print(ex.toString() + " >> Closing DB");
        }
    }
    
    private void flushStatementOnly() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            System.err.print(ex.toString() + " >> Closing DB");
        }
    }
}
