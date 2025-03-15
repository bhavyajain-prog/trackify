package com.nerfex.model;

import java.sql.Date;

public class Transaction {
    private int id;
    private int walletId;
    private double amount;
    private Date transactionDate;
    private String item;
    private String description;
    private String type;
    private String paymentMethod;
    private String category;

    public Transaction(int id, int walletId, double amount, Date transactionDate, String item, String description,
            String type, String paymentMethod, String category) {
        this.id = id;
        this.walletId = walletId;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.item = item;
        this.description = description;
        this.type = type;
        this.paymentMethod = paymentMethod;
        this.category = category;
    }

    public Transaction(int walletId, double amount, Date transactionDate, String item, String description, String type,
            String paymentMethod, String category) {
        this.walletId = walletId;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.item = item;
        this.description = description;
        this.type = type;
        this.paymentMethod = paymentMethod;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public int getWalletId() {
        return walletId;
    }

    public double getAmount() {
        return amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public String getItem() {
        return item;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getCategory() {
        return category;
    }
}
