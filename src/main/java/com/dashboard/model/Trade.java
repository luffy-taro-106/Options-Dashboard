package com.dashboard.model;

import java.time.LocalDateTime;

public class Trade {
    private int id;
    private int userId;
    private int optionId;
    private int quantity;
    private double price;
    private String direction; // "BUY" or "SELL"
    private LocalDateTime timestamp;

    public Trade() {}

    public Trade(int userId, int optionId, int quantity, double price, String direction, LocalDateTime timestamp) {
        this.userId = userId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.price = price;
        this.direction = direction;
        this.timestamp = timestamp;
    }

    // ✔️ Tip: Don’t forget to generate these (or do them manually)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getOptionId() { return optionId; }
    public void setOptionId(int optionId) { this.optionId = optionId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
