package com.dashboard.cli;

import com.dashboard.dao.DBConnection;
import com.dashboard.model.Trade;

import java.sql.*;
import java.util.*;

public class PortfolioView {

    public static void showPortfolio(int userId) {
        String sql = "SELECT o.ticker, o.strike, o.type, o.bid, o.ask, " +
                "t.quantity, t.price, t.direction, t.timestamp " +
                "FROM trades t " +
                "JOIN options o ON t.option_id = o.id " +
                "WHERE t.user_id = ? " +
                "ORDER BY o.ticker, o.strike, o.type, t.timestamp";


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n🧾 Portfolio Summary:");
            System.out.printf("%-5s %-6s %-6s %-7s %-7s %-6s %-8s %-8s\n",
                    "TICK", "TYPE", "STRIKE", "BID", "ASK", "QTY", "DIR", "COST");

            double totalCost = 0;
            double totalMarket = 0;

            while (rs.next()) {
                String ticker = rs.getString("ticker");
                String type = rs.getString("type");
                double strike = rs.getDouble("strike");
                double bid = rs.getDouble("bid");
                double ask = rs.getDouble("ask");
                int qty = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String dir = rs.getString("direction");

                double cost = qty * price;
                double marketValue = qty * (dir.equals("BUY") ? bid : ask);

                totalCost += dir.equals("BUY") ? cost : -cost;
                totalMarket += marketValue;

                System.out.printf("%-5s %-6s %-6.2f %-7.2f %-7.2f %-6d %-8s %-8.2f\n",
                        ticker, type, strike, bid, ask, qty, dir, cost);
            }

            double pnl = totalMarket - totalCost;

            System.out.println("\n💼 Total Cost Basis: ₹" + totalCost);
            System.out.println("📈 Current Market Value: ₹" + totalMarket);
            System.out.printf("💹 Unrealized P&L: %s₹%.2f\n", pnl >= 0 ? "🟢 " : "🔴 ", pnl);

        } catch (Exception e) {
            System.err.println("❌ Failed to show portfolio: " + e.getMessage());
        }
    }
}
