package com.nerfex.dao.impl;

import com.nerfex.dao.WalletDAO;
import com.nerfex.database.DatabaseConnector;
import com.nerfex.model.Wallet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WalletDAOImpl implements WalletDAO {

    @Override
    public void addWallet(Wallet wallet) {
        String sql = "INSERT INTO wallets (name, balance) VALUES (?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, wallet.getName());
            stmt.setDouble(2, wallet.getBalance());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Wallet getWalletById(int id) {
        String sql = "SELECT * FROM wallets WHERE id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Wallet(rs.getInt("id"), rs.getString("name"), rs.getDouble("balance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Wallet> getAllWallets() {
        List<Wallet> wallets = new ArrayList<>();
        String sql = "SELECT * FROM wallets";
        try (Connection conn = DatabaseConnector.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                wallets.add(new Wallet(rs.getInt("id"), rs.getString("name"), rs.getDouble("balance")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wallets;
    }

    @Override
    public void updateWalletBalance(int id, double newBalance) {
        String sql = "UPDATE wallets SET balance = ? WHERE id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteWallet(int id) {
        String sql = "DELETE FROM wallets WHERE id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            sql = "DELETE FROM transactions WHERE wallet_id = ?";
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
