package com.nerfex;

import com.nerfex.dao.TransactionDAO;
import com.nerfex.dao.WalletDAO;
import com.nerfex.dao.impl.TransactionDAOImpl;
import com.nerfex.dao.impl.WalletDAOImpl;
import com.nerfex.database.DatabaseInitializer;
import com.nerfex.model.Transaction;
import com.nerfex.model.Wallet;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class TrackifyCLI {
    
    private final WalletDAO walletDAO;
    private final TransactionDAO transactionDAO;
    private final Scanner scanner;
    
    public TrackifyCLI() {
        DatabaseInitializer.initialize();
        this.walletDAO = new WalletDAOImpl();
        this.transactionDAO = new TransactionDAOImpl();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n===== TRACKIFY MENU =====");
            System.out.println("1. Add Wallet");
            System.out.println("2. View Wallets");
            System.out.println("3. Delete Wallet");
            System.out.println("4. Add Transaction");
            System.out.println("5. View Transactions");
            System.out.println("6. Delete Transaction");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addWallet();
                case 2 -> viewWallets();
                case 3 -> deleteWallet();
                case 4 -> addTransaction();
                case 5 -> viewTransactions();
                case 6 -> deleteTransactions();
                case 0 -> {
                    System.out.println("Exiting... Thank you for using Trackify!");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private void addWallet() {
        System.out.print("Enter wallet name: ");
        String name = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        Wallet wallet = new Wallet(0, name, balance);
        walletDAO.addWallet(wallet);
        System.out.println("Wallet added successfully!");
    }

    private void viewWallets() {
        List<Wallet> wallets = walletDAO.getAllWallets();
        System.out.println("\n===== Wallets =====");
        for (Wallet wallet : wallets) {
            System.out.println(wallet.getId() + " | " + wallet.getName() + " | Balance: " + wallet.getBalance());
        }
    }

    private void deleteWallet() {
        System.out.print("Enter wallet ID: ");
        int walletId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        walletDAO.deleteWallet(walletId);
        System.out.println("Wallet deleted successfully!");
    }

    private void addTransaction() {
        System.out.print("Enter wallet ID: ");
        int walletId = scanner.nextInt();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter item: ");
        String item = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter type (DEPOSIT/WITHDRAWAL): ");
        String type = scanner.nextLine().toUpperCase();
        System.out.print("Enter payment method (CASH/UPI): ");
        String paymentMethod = scanner.nextLine().toUpperCase();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        Date transactionDate = new Date(System.currentTimeMillis());

        Transaction transaction = new Transaction(walletId, amount, transactionDate, item, description, type,
                paymentMethod, category);
        transactionDAO.addTransaction(transaction);
        System.out.println("Transaction added successfully!");
    }

    private void viewTransactions() {
        List<Transaction> transactions = transactionDAO.getAllTransactions();
        System.out.println("\n===== All Transactions =====");
        for (Transaction transaction : transactions) {
            System.out.println(transaction.getId() + " | " + transaction.getItem() + " | Amount: "
                    + transaction.getAmount() + " | Category: " + transaction.getCategory());
        }
    }

    private void deleteTransactions() {
        System.out.print("Enter transaction ID: ");
        int transactionId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        transactionDAO.deleteTransaction(transactionId);
        System.out.println("Transaction deleted successfully!");
    }

    public static void main(String[] args) {
        TrackifyCLI cli = new TrackifyCLI();
        cli.start();
    }
}
