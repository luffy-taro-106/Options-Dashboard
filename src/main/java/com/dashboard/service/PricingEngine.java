package com.dashboard.service;

import com.dashboard.model.Option;

public class PricingEngine {

    // Cumulative Normal Distribution Function
    private static double N(double x) {
        return 0.5 * (1.0 + erf(x / Math.sqrt(2.0)));
    }

    // Error function (needed for cumulative normal)
    private static double erf(double z) {
        double t = 1.0 / (1.0 + 0.5 * Math.abs(z));
        double ans = 1 - t * Math.exp(-z*z - 1.26551223 +
                t * (1.00002368 +
                        t * (0.37409196 +
                                t * (0.09678418 +
                                        t * (-0.18628806 +
                                                t * (0.27886807 +
                                                        t * (-1.13520398 +
                                                                t * (1.48851587 +
                                                                        t * (-0.82215223 +
                                                                                t * 0.17087277)))))))));
        return z >= 0 ? ans : -ans;
    }

    public static double[] calculateBlackScholes(Option opt, double S, double r, double T) {
        double K = opt.getStrike();
        double sigma = opt.getIv(); // Implied volatility (as decimal, e.g. 0.2 for 20%)

        double d1 = (Math.log(S / K) + (r + sigma*sigma/2) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);

        double price, delta;
        if (opt.getType().equalsIgnoreCase("CALL")) {
            price = S * N(d1) - K * Math.exp(-r * T) * N(d2);
            delta = N(d1);
        } else {
            price = K * Math.exp(-r * T) * N(-d2) - S * N(-d1);
            delta = -N(-d1);
        }

        double gamma = Math.exp(-d1*d1/2) / (S * sigma * Math.sqrt(2 * Math.PI * T));
        double vega = S * Math.sqrt(T) * Math.exp(-d1*d1/2) / Math.sqrt(2 * Math.PI);
        double theta = - (S * sigma * Math.exp(-d1*d1/2)) / (2 * Math.sqrt(2 * Math.PI * T));

        return new double[] {price, delta, gamma, vega, theta};
    }
}
