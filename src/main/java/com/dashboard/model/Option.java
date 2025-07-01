package com.dashboard.model;

public class Option {
    private int id;
    private String ticker;
    private double strike;
    private String expiry;
    private String type; // "CALL" or "PUT"
    private double bid;
    private double ask;
    private double iv; // implied volatility

    public Option() {}

    public Option(String ticker, double strike, String expiry, String type, double bid, double ask, double iv) {
        this.ticker = ticker;
        this.strike = strike;
        this.expiry = expiry;
        this.type = type;
        this.bid = bid;
        this.ask = ask;
        this.iv = iv;
    }

    // ✅ Add these getters
    public String getTicker() { return ticker; }
    public double getStrike() { return strike; }
    public String getExpiry() { return expiry; }
    public String getType() { return type; }
    public double getBid() { return bid; }
    public double getAsk() { return ask; }
    public double getIv() { return iv; }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

}
