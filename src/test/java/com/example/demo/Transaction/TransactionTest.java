package com.example.demo.Transaction;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class TransactionTest {

    @Test
    void ConstructorTest() {
        Transaction actualTransaction = new Transaction();
        assertNull(actualTransaction.getTransAmount());
        assertNull(actualTransaction.getTransID());
        assertNull(actualTransaction.getCardNumber());
        assertNull(actualTransaction.getMerchant());
        assertNull(actualTransaction.getTransCategory());
        assertNull(actualTransaction.getUsername());
        assertNull(actualTransaction.getTransDate());
    }
}
