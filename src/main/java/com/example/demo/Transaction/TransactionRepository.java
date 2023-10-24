package com.example.demo.Transaction;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository <Transaction, Long> {

    @Query("SELECT s from Transaction s WHERE s.username= ?1 ORDER BY s.transDate DESC")
    List<Transaction> findTransactionByUsername(String username);

    @Query("SELECT s from Transaction s WHERE s.username= ?1 and s.transCategory= ?2 ORDER BY s.transDate DESC")
    List<Transaction> findTransactionByCategory(String username, String transCategory);

    @Query("SELECT s from Transaction s WHERE s.username= ?1 and s.merchant= ?2 ORDER BY s.transDate DESC")
    List<Transaction> findTransactionByMerchant(String username, String merchant);

    @Query("SELECT s from Transaction s WHERE s.username= ?1 and year(s.transDate)= ?2 ORDER BY s.transDate DESC")
    List<Transaction> findTransactionByYear(String username, Integer year);

    @Query("SELECT s from Transaction s WHERE s.username= ?1 and month(s.transDate)= ?2 ORDER BY s.transDate DESC")
    List<Transaction> findTransactionByMonth(String username, Integer month);
}
