package com.nerfex.dao;

import com.nerfex.model.Transaction;
import java.util.List;

public interface TransactionDAO {
    void addTransaction(Transaction transaction);

    List<Transaction> getTransactionsByWalletId(int walletId);

    List<Transaction> getAllTransactions();

    List<Transaction> getTransactionsByDateRange(String startDate, String endDate);

    List<Transaction> getTransactionsByCategory(String category);

    void deleteTransaction(int transactionId);
}
