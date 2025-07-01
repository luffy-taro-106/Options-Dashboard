package com.dashboard.dao;

import com.dashboard.model.Trade;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TradeDAO {

    public static void insertTrade(Trade trade) {
        String sql = "INSERT INTO trades (user_id, option_id, quantity, price, direction, timestamp) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, trade.getUserId());
            pstmt.setInt(2, trade.getOptionId());
            pstmt.setInt(3, trade.getQuantity());
            pstmt.setDouble(4, trade.getPrice());
            pstmt.setString(5, trade.getDirection());
            pstmt.setString(6, trade.getTimestamp().toString());

            pstmt.executeUpdate();
            System.out.println("✅ Trade recorded.");
        } catch (Exception e) {
            System.err.println("❌ Failed to insert trade: " + e.getMessage());
        }
    }

    public static List<Trade> getTradesByUser(int userId) {
        List<Trade> trades = new ArrayList<>();
        String sql = "SELECT * FROM trades WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Trade t = new Trade(
                        rs.getInt("user_id"),
                        rs.getInt("option_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("direction"),
                        LocalDateTime.parse(rs.getString("timestamp"))
                );
                t.setId(rs.getInt("id"));
                trades.add(t);
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to fetch trades: " + e.getMessage());
        }

        return trades;
    }
}
