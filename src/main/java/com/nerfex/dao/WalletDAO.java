package com.nerfex.dao;

import java.util.List;

import com.nerfex.model.Wallet;

public interface WalletDAO {
    void addWallet(Wallet wallet);

    Wallet getWalletById(int id);

    List<Wallet> getAllWallets();

    void updateWalletBalance(int id, double balance);

    void deleteWallet(int id);
}
