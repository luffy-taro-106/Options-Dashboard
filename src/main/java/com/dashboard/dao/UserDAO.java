package com.dashboard.dao;

import com.dashboard.model.User;
import java.sql.*;

public class UserDAO {

    public static void addUser(User user) {
        String sql = "INSERT INTO users(name) VALUES(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.executeUpdate();
            System.out.println("✅ User added: " + user.getName());
        } catch (Exception e) {
            System.err.println("❌ Failed to add user: " + e.getMessage());
        }
    }

    public static User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("name"));
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to get user: " + e.getMessage());
        }
        return null;
    }
}
