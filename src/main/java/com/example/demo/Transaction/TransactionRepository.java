package com.example.demo.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface TransactionRepository extends JpaRepository <Transaction, Long> {

    @Query("SELECT s from Transaction s WHERE s.username= ?1 ORDER BY s.transDate DESC")
    List<Transaction> findTransactionByUsername(String username);

    @Query("SELECT s from Transaction s WHERE s.username= ?1 and s.transCategory= ?2 ORDER BY s.transDate DESC")
    List<Transaction> findTransactionByCategory(String username, String transCategory);

    @Query("SELECT s from Transaction s WHERE s.username=?1 and s.merchant= ?2 ORDER BY s.transDate DESC")
    List<Transaction> findTransactionByMerchant(String username, String merchant);

    @Query("SELECT s from Transaction s WHERE s.username= ?1 and s.cardNumber= ?2 and year(s.transDate)= ?3 ORDER BY s.transDate DESC")
    List<Transaction> findTransactionByYear(String username, String cardNumber, Integer year);

    @Query("SELECT s from Transaction s WHERE s.username= ?1 and s.cardNumber= ?2 and month(s.transDate)= ?3 and year(s.transDate)= ?4 ORDER BY s.transDate DESC")
    List<Transaction> findTransactionByMonth(String username,String cardNumber, Integer month, Integer year);

    @Query("SELECT s from Transaction s WHERE s.username= ?1 and s.cardNumber= ?2 and month(s.transDate)= ?3 and year(s.transDate)= ?4 and day(s.transDate)= ?5 ORDER BY s.transDate DESC")
    List<Transaction> findTransactionByDate(String username,String cardNumber, Integer month, Integer year, Integer day);

    @Query("SELECT s from Transaction s WHERE s.username= ?1 and s.cardNumber= ?2 ORDER BY s.transDate DESC")
    List<Transaction> findTransactionByCardNumber(String username, String cardNumber);

    @Query(value = "SELECT SUM(trans_amount) FROM Transaction WHERE username = ?1 AND card_number = ?2 AND trans_date < ?3", nativeQuery = true)
    Optional<Double> findBalanceUpToDate(String username, String cardNumber, ZonedDateTime endDate);
}
