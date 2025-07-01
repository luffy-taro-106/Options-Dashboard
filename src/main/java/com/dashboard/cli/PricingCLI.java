package com.dashboard.cli;

import com.dashboard.model.Option;
import com.dashboard.service.PricingEngine;

import java.util.Scanner;

public class PricingCLI {

    public static void runPricingMenu(Option opt) {
        Scanner sc = new Scanner(System.in);

        System.out.print("📈 Enter current stock price (S): ");
        double S = sc.nextDouble();

        System.out.print("💵 Enter risk-free rate (r, e.g. 0.05 for 5%): ");
        double r = sc.nextDouble();

        System.out.print("🕒 Enter time to expiry (T in years): ");
        double T = sc.nextDouble();

        double[] result = PricingEngine.calculateBlackScholes(opt, S, r, T);

        System.out.printf("\n📊 Option: %s %s %.2f\n", opt.getTicker(), opt.getType(), opt.getStrike());
        System.out.printf("💰 Black-Scholes Price: ₹%.2f\n", result[0]);
        System.out.printf("🧭 Delta: %.4f | 🎯 Gamma: %.4f\n", result[1], result[2]);
        System.out.printf("⚡ Vega: %.4f | ⏳ Theta: %.4f\n", result[3], result[4]);
    }
}
