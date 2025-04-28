package hotel.databaseOperation;

import hotel.classes.Room;
import hotel.classes.RoomFare;
import javax.swing.JOptionPane;
import java.sql.*;

public class RoomDb {
    Connection conn = DataBaseConnection.connectTODB();
    PreparedStatement statement = null;
    ResultSet result = null;

    public void insertRoom(Room room) {
        try {
            String insertQuery = "INSERT INTO room(room_no, bed_number, tv, wifi, gizer, phone, room_class) VALUES (?, ?, ?, ?, ?, ?, ?)";
            statement = conn.prepareStatement(insertQuery);
            statement.setString(1, room.getRoomNo());
            statement.setInt(2, room.getBedNumber());
            statement.setString(3, boolToString(room.isHasTV()));
            statement.setString(4, boolToString(room.isHasWIFI()));
            statement.setString(5, boolToString(room.isHasGizer()));
            statement.setString(6, boolToString(room.isHasPhone()));
            statement.setString(7, room.getRoomClass().getRoomType());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully inserted a new Room");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nInsert Room Failed");
        } finally {
            closeStatement();
        }
    }

    public void updateRoom(Room room) {
        try {
            String updateQuery = "UPDATE room SET room_no = ?, bed_number = ?, tv = ?, wifi = ?, gizer = ?, phone = ?, room_class = ? WHERE room_id = ?";
            statement = conn.prepareStatement(updateQuery);
            statement.setString(1, room.getRoomNo());
            statement.setInt(2, room.getBedNumber());
            statement.setString(3, boolToString(room.isHasTV()));
            statement.setString(4, boolToString(room.isHasWIFI()));
            statement.setString(5, boolToString(room.isHasGizer()));
            statement.setString(6, boolToString(room.isHasPhone()));
            statement.setString(7, room.getRoomClass().getRoomType());
            statement.setInt(8, room.getRoomId());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully updated Room");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nUpdate Room Failed");
        } finally {
            closeStatement();
        }
    }

    public String boolToString(boolean value) {
        return value ? "true" : "false";
    }

    private void closeStatement() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            System.err.println(ex.toString() + " >> Closing Statement");
        }
    }
}
