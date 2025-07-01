package com.dashboard;

import com.dashboard.api.OptionFetcher;
import com.dashboard.dao.*;
import com.dashboard.model.*;
import com.dashboard.cli.PortfolioView;
import com.dashboard.service.PricingEngine;
import com.dashboard.model.Option;
import com.dashboard.cli.PricingCLI;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DBConnection.getConnection()) {
                if (conn != null) {
                    System.out.println("✅ Connected to SQLite database.");

                    SchemaInitializer.initialize();

                    // Add or fetch user (Alice)
                    User u = new User("Alice");
                    UserDAO.addUser(u);
                    User fetched = UserDAO.getUserById(1);
                    if (fetched == null) {
                        System.err.println("⚠️ User not found.");
                        return;
                    }

                    int userId = fetched.getId();
                    String ticker = "AAPL";

                    // Fetch options from yfinance microservice
                    List<Option> options = OptionFetcher.fetchOptions(ticker);
                    if (options.isEmpty()) {
                        System.out.println("⚠️ No options fetched.");
                        return;
                    }

                    // Store options in DB
                    for (Option opt : options) {
                        int optId = OptionDAO.insertOption(opt);
                        opt.setId(optId);
                    }

                    // Show top 5
                    System.out.println("\n📋 Options loaded. Showing top 5:");
                    for (int i = 0; i < Math.min(5, options.size()); i++) {
                        Option o = options.get(i);
                        System.out.printf("%d) %s %s Strike: %.2f Bid: %.2f Ask: %.2f\n",
                                i + 1, o.getTicker(), o.getType(), o.getStrike(), o.getBid(), o.getAsk());
                    }

                    // CLI Input
                    Scanner sc = new Scanner(System.in);
                    System.out.print("\nEnter option # to trade: ");
                    int choice = sc.nextInt() - 1;

                    if (choice < 0 || choice >= options.size()) {
                        System.out.println("❌ Invalid option selected.");
                        return;
                    }

                    Option selected = options.get(choice);

                    PricingCLI.runPricingMenu(selected);

                    // Estimate Greeks before placing trade
                    double S = 185.0;    // Underlying price (can later fetch via API)
                    double r = 0.05;     // 5% risk-free rate
                    double T = 0.25;     // 3 months to expiry

                    double[] results = PricingEngine.calculateBlackScholes(selected, S, r, T);

                    System.out.println("\n📐 Option: " + selected.getTicker() + " " + selected.getType() + " " + selected.getStrike());
                    System.out.printf("💰 BS Price: %.2f\n", results[0]);
                    System.out.printf("🧭 Delta: %.4f | 🎯 Gamma: %.4f\n", results[1], results[2]);
                    System.out.printf("⚡ Vega: %.4f | ⏳ Theta: %.4f\n", results[3], results[4]);


                    System.out.print("Buy or Sell? ");
                    String direction = sc.next().toUpperCase();

                    System.out.print("Quantity: ");
                    int qty = sc.nextInt();

                    Trade trade = new Trade(userId, selected.getId(), qty, selected.getAsk(), direction, LocalDateTime.now());
                    TradeDAO.insertTrade(trade);

                    System.out.println("💰 Trade complete!");

                    PortfolioView.showPortfolio(userId);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ App failed: " + e.getMessage());
        }
    }
}
