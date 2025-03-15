package com.nerfex.dao.impl;

import com.nerfex.dao.TransactionDAO;
import com.nerfex.database.DatabaseConnector;
import com.nerfex.model.Transaction;
import com.nerfex.model.Wallet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {
    private final WalletDAOImpl walletDAO = new WalletDAOImpl();

    @Override
    public void addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (wallet_id, amount, transaction_date, item, description, type, payment_method, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, transaction.getWalletId());
            stmt.setDouble(2, transaction.getAmount());
            stmt.setDate(3, transaction.getTransactionDate());
            stmt.setString(4, transaction.getItem());
            stmt.setString(5, transaction.getDescription());
            stmt.setString(6, transaction.getType());
            stmt.setString(7, transaction.getPaymentMethod());
            stmt.setString(8, transaction.getCategory());
            stmt.executeUpdate();
            Wallet wallet = walletDAO.getWalletById(transaction.getWalletId());
            if (transaction.getType().equals("WITHDRAWAL"))
                walletDAO.updateWalletBalance(wallet.getId(), wallet.getBalance() - transaction.getAmount());
            else if (transaction.getType().equals("DEPOSIT"))
                walletDAO.updateWalletBalance(wallet.getId(), wallet.getBalance() + transaction.getAmount());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> getTransactionsByWalletId(int walletId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE wallet_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, walletId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("id"),
                        rs.getInt("wallet_id"),
                        rs.getDouble("amount"),
                        rs.getDate("transaction_date"),
                        rs.getString("item"),
                        rs.getString("description"),
                        rs.getString("type"),
                        rs.getString("payment_method"),
                        rs.getString("category")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (Connection conn = DatabaseConnector.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("id"),
                        rs.getInt("wallet_id"),
                        rs.getDouble("amount"),
                        rs.getDate("transaction_date"),
                        rs.getString("item"),
                        rs.getString("description"),
                        rs.getString("type"),
                        rs.getString("payment_method"),
                        rs.getString("category")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionsByDateRange(String startDate, String endDate) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE transaction_date BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("id"),
                        rs.getInt("wallet_id"),
                        rs.getDouble("amount"),
                        rs.getDate("transaction_date"),
                        rs.getString("item"),
                        rs.getString("description"),
                        rs.getString("type"),
                        rs.getString("payment_method"),
                        rs.getString("category")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionsByCategory(String category) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE category = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("id"),
                        rs.getInt("wallet_id"),
                        rs.getDouble("amount"),
                        rs.getDate("transaction_date"),
                        rs.getString("item"),
                        rs.getString("description"),
                        rs.getString("type"),
                        rs.getString("payment_method"),
                        rs.getString("category")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public void deleteTransaction(int transactionId) {
        String fetchSql = "SELECT wallet_id, amount, type FROM transactions WHERE id = ?";
        String deleteSql = "DELETE FROM transactions WHERE id = ?";

        try (PreparedStatement fetchStmt = DatabaseConnector.getConnection().prepareStatement(fetchSql)) {

            // Step 1: Fetch the transaction details
            fetchStmt.setInt(1, transactionId);
            ResultSet rs = fetchStmt.executeQuery();

            if (rs.next()) {
                int walletId = rs.getInt("wallet_id");
                double amount = rs.getDouble("amount");
                String type = rs.getString("type");

                // Step 2: Fetch wallet details
                Wallet wallet = walletDAO.getWalletById(walletId);
                if (wallet != null) {
                    // Step 3: Reverse the transaction effect on the wallet
                    if (type.equals("WITHDRAWAL")) {
                        walletDAO.updateWalletBalance(wallet.getId(), wallet.getBalance() + amount);
                    } else if (type.equals("DEPOSIT")) {
                        walletDAO.updateWalletBalance(wallet.getId(), wallet.getBalance() - amount);
                    }
                }
            } else {
                System.out.println("Transaction not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Step 4: Delete the transaction in a separate try block
        try (PreparedStatement deleteStmt = DatabaseConnector.getConnection().prepareStatement(deleteSql)) {
            deleteStmt.setInt(1, transactionId);
            deleteStmt.executeUpdate();
            System.out.println("Transaction deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
