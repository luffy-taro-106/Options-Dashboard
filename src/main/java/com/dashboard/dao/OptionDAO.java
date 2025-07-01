package com.dashboard.dao;

import com.dashboard.model.Option;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OptionDAO {

    public static int insertOption(Option opt) {
        String sql = "INSERT INTO options(ticker, strike, expiry, type, bid, ask, iv) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, opt.getTicker());
            pstmt.setDouble(2, opt.getStrike());
            pstmt.setString(3, opt.getExpiry());
            pstmt.setString(4, opt.getType());
            pstmt.setDouble(5, opt.getBid());
            pstmt.setDouble(6, opt.getAsk());
            pstmt.setDouble(7, opt.getIv());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            System.err.println("❌ Failed to insert option: " + e.getMessage());
        }
        return -1;
    }

    public static List<Option> getAllOptions() {
        List<Option> options = new ArrayList<>();
        String sql = "SELECT * FROM options";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Option o = new Option(
                        rs.getString("ticker"),
                        rs.getDouble("strike"),
                        rs.getString("expiry"),
                        rs.getString("type"),
                        rs.getDouble("bid"),
                        rs.getDouble("ask"),
                        rs.getDouble("iv")
                );
                o.setId(rs.getInt("id"));
                options.add(o);
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to fetch options: " + e.getMessage());
        }
        return options;
    }
}
