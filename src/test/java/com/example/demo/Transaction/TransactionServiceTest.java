package com.example.demo.Transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.CreditCard.CreditCard;
import com.example.demo.CreditCard.CreditCardService;
import com.example.demo.Customer.CustomerRepository;
import com.example.demo.Exception.RequestException;
import com.example.demo.Exception.SuccessfulRequest;

import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.ZoneOffset;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TransactionService.class})
@ExtendWith(SpringExtension.class)
class TransactionServiceTest {
    @MockBean
    private CreditCardService creditCardService;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @Test
    void addNewTransactionTest() {
        Transaction transaction = new Transaction();
        transaction.setCardNumber("42");
        transaction.setMerchant("Merchant");
        transaction.setTransAmount(10.0d);
        transaction.setTransCategory("Trans Category");
        transaction
                .setTransDate(java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setTransID(1L);
        transaction.setUsername("janedoe");
        when(transactionRepository.save(Mockito.<Transaction>any())).thenReturn(transaction);
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);

        CreditCard creditCard = new CreditCard();
        creditCard.setAvailableCredit(10.0d);
        creditCard.setCardNumber("42");
        creditCard.setCreditLimit(10.0d);
        creditCard.setCurrentBalance(10.0d);
        creditCard.setDueDate(mock(java.sql.Date.class));
        creditCard.setInterestRate(10.0d);
        creditCard.setIssuer("Issuer");
        creditCard.setMinimumPayment(10.0d);
        creditCard.setPaymentDue(10.0d);
        creditCard.setUsername("janedoe");
        when(creditCardService.doesCreditCardExist(Mockito.<String>any())).thenReturn(creditCard);
        doNothing().when(creditCardService).updateBalance(Mockito.<Transaction>any());

        Transaction transaction2 = new Transaction();
        transaction2.setCardNumber("42");
        transaction2.setMerchant("Merchant");
        transaction2.setTransAmount(10.0d);
        transaction2.setTransCategory("Trans Category");
        transaction2
                .setTransDate(java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction2.setTransID(1L);
        transaction2.setUsername("janedoe");
        transactionService.addNewTransaction(transaction2, "janedoe");
        verify(creditCardService).doesCreditCardExist(Mockito.<String>any());
        verify(creditCardService).updateBalance(Mockito.<Transaction>any());
        verify(customerRepository).existsById(Mockito.<String>any());
        verify(transactionRepository).save(Mockito.<Transaction>any());
        assertEquals("janedoe", transaction2.getUsername());
    }
    @Test
    void getTransactionTest() {
        ArrayList<Transaction> transactionList = new ArrayList<>();
        when(transactionRepository.findTransactionByUsername(Mockito.<String>any())).thenReturn(transactionList);
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        List<Transaction> actualTransaction = transactionService.getTransaction("janedoe");
        verify(transactionRepository).findTransactionByUsername(Mockito.<String>any());
        verify(customerRepository).existsById(Mockito.<String>any());
        assertTrue(actualTransaction.isEmpty());
        assertSame(transactionList, actualTransaction);
    }
    @Test
    void cannotGetTransactionByCategoryTest() {
        when(transactionRepository.findTransactionByCategory(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ArrayList<>());
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        assertThrows(SuccessfulRequest.class,
                () -> transactionService.getTransactionByCategory("janedoe", "Trans Category"));
        verify(transactionRepository).findTransactionByCategory(Mockito.<String>any(), Mockito.<String>any());
        verify(customerRepository).existsById(Mockito.<String>any());
    }
    @Test
    void getTransactionByCategoryTest() {
        Transaction transaction = new Transaction();
        transaction.setCardNumber("42");
        transaction.setMerchant("There is no such transaction");
        transaction.setTransAmount(10.0d);
        transaction.setTransCategory("There is no such transaction");
        transaction.setTransDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setTransID(1L);
        transaction.setUsername("janedoe");

        ArrayList<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        when(transactionRepository.findTransactionByCategory(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(transactionList);
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        List<Transaction> actualTransactionByCategory = transactionService.getTransactionByCategory("janedoe",
                "Trans Category");
        verify(transactionRepository, atLeast(1)).findTransactionByCategory(Mockito.<String>any(), Mockito.<String>any());
        verify(customerRepository).existsById(Mockito.<String>any());
        assertEquals(1, actualTransactionByCategory.size());
        assertSame(transactionList, actualTransactionByCategory);
    }
    @Test
    void getTransactionByMerchantTest() {
        Transaction transaction = new Transaction();
        transaction.setCardNumber("42");
        transaction.setMerchant("There is no such transaction");
        transaction.setTransAmount(10.0d);
        transaction.setTransCategory("There is no such transaction");
        transaction.setTransDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setTransID(1L);
        transaction.setUsername("janedoe");

        ArrayList<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        when(transactionRepository.findTransactionByMerchant(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(transactionList);
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        List<Transaction> actualTransactionByMerchant = transactionService.getTransactionByMerchant("janedoe", "Merchant");
        verify(transactionRepository, atLeast(1)).findTransactionByMerchant(Mockito.<String>any(), Mockito.<String>any());
        verify(customerRepository).existsById(Mockito.<String>any());
        assertEquals(1, actualTransactionByMerchant.size());
        assertSame(transactionList, actualTransactionByMerchant);
    }
    @Test
    void cannotGetTransactionByMerchantTest() {
        when(transactionRepository.findTransactionByMerchant(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ArrayList<>());
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        assertThrows(SuccessfulRequest.class, () -> transactionService.getTransactionByMerchant("janedoe", "Merchant"));
        verify(transactionRepository).findTransactionByMerchant(Mockito.<String>any(), Mockito.<String>any());
        verify(customerRepository).existsById(Mockito.<String>any());
    }
    @Test
    void cannotGetTransactionByYearTest() {
        when(transactionRepository.findTransactionByYear(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any())).thenReturn(new ArrayList<>());
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        assertThrows(SuccessfulRequest.class, () -> transactionService.getTransactionByYear("janedoe", "42", 1));
        verify(transactionRepository).findTransactionByYear(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any());
        verify(customerRepository).existsById(Mockito.<String>any());
    }
    @Test
    void getTransactionByYear() {
        Transaction transaction = new Transaction();
        transaction.setCardNumber("42");
        transaction.setMerchant("There is no such transaction");
        transaction.setTransAmount(10.0d);
        transaction.setTransCategory("There is no such transaction");
        transaction.setTransDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setTransID(1L);
        transaction.setUsername("janedoe");

        ArrayList<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        when(transactionRepository.findTransactionByYear(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any())).thenReturn(transactionList);
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        List<Transaction> actualTransactionByYear = transactionService.getTransactionByYear("janedoe", "42", 1);
        verify(transactionRepository, atLeast(1)).findTransactionByYear(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any());
        verify(customerRepository).existsById(Mockito.<String>any());
        assertEquals(1, actualTransactionByYear.size());
        assertSame(transactionList, actualTransactionByYear);
    }
    @Test
    void getTransactionByMonthTest() {
        ArrayList<Transaction> transactionList = new ArrayList<>();
        when(transactionRepository.findTransactionByMonth(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any(), Mockito.<Integer>any())).thenReturn(transactionList);
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        List<Transaction> actualTransactionByMonth = transactionService.getTransactionByMonth("janedoe", "42", 1, 1);
        verify(transactionRepository).findTransactionByMonth(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any(), Mockito.<Integer>any());
        verify(customerRepository).existsById(Mockito.<String>any());
        assertTrue(actualTransactionByMonth.isEmpty());
        assertSame(transactionList, actualTransactionByMonth);
    }

    @Test
    void getTransactionByDayTest() {
        ArrayList<Transaction> transactionList = new ArrayList<>();
        when(transactionRepository.findTransactionByDate(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Integer>any())).thenReturn(transactionList);
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        List<Transaction> actualTransactionByDay = transactionService.getTransactionByDay("janedoe", "42", 1, 1, 1);
        verify(transactionRepository).findTransactionByDate(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Integer>any());
        verify(customerRepository).existsById(Mockito.<String>any());
        assertTrue(actualTransactionByDay.isEmpty());
        assertSame(transactionList, actualTransactionByDay);
    }
    @Test
    void cannotGetTransactionByCardNumberTest() {
        when(transactionRepository.findTransactionByCardNumber(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ArrayList<>());
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        assertThrows(SuccessfulRequest.class, () -> transactionService.getTransactionByCardNumber("janedoe", "42"));
        verify(transactionRepository).findTransactionByCardNumber(Mockito.<String>any(), Mockito.<String>any());
        verify(customerRepository).existsById(Mockito.<String>any());
    }
    @Test
    void getTransactionByCardNumberTest() {
        Transaction transaction = new Transaction();
        transaction.setCardNumber("42");
        transaction.setMerchant("There is no such transaction");
        transaction.setTransAmount(10.0d);
        transaction.setTransCategory("There is no such transaction");
        transaction.setTransDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setTransID(1L);
        transaction.setUsername("janedoe");

        ArrayList<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        when(transactionRepository.findTransactionByCardNumber(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(transactionList);
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        List<Transaction> actualTransactionByCardNumber = transactionService.getTransactionByCardNumber("janedoe", "42");
        verify(transactionRepository, atLeast(1)).findTransactionByCardNumber(Mockito.<String>any(), Mockito.<String>any());
        verify(customerRepository).existsById(Mockito.<String>any());
        assertEquals(1, actualTransactionByCardNumber.size());
        assertSame(transactionList, actualTransactionByCardNumber);
    }


    @Test
    void deleteTransactionTest() {
        Transaction transaction = new Transaction();
        transaction.setCardNumber("42");
        transaction.setMerchant("Merchant");
        transaction.setTransAmount(10.0d);
        transaction.setTransCategory("Trans Category");
        transaction.setTransDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setTransID(1L);
        transaction.setUsername("janedoe");
        Optional<Transaction> ofResult = Optional.of(transaction);
        doNothing().when(transactionRepository).deleteById(Mockito.<Long>any());
        when(transactionRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(creditCardService).updateBalance(Mockito.<Transaction>any());
        transactionService.deleteTransaction(1L, "janedoe");
        verify(creditCardService).updateBalance(Mockito.<Transaction>any());
        verify(transactionRepository).deleteById(Mockito.<Long>any());
        verify(transactionRepository).findById(Mockito.<Long>any());
    }
    @Test
    void cannotDeleteNonExistTransaction() {
        Transaction transaction = new Transaction();
        transaction.setCardNumber("42");
        transaction.setMerchant("Merchant");
        transaction.setTransAmount(10.0d);
        transaction.setTransCategory("Trans Category");
        transaction.setTransDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setTransID(1L);
        transaction.setUsername("janedoe");
        Optional<Transaction> ofResult = Optional.of(transaction);
        when(transactionRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doThrow(new SuccessfulRequest("An error occurred")).when(creditCardService)
                .updateBalance(Mockito.<Transaction>any());
        assertThrows(SuccessfulRequest.class, () -> transactionService.deleteTransaction(1L, "janedoe"));
        verify(creditCardService).updateBalance(Mockito.<Transaction>any());
        verify(transactionRepository).findById(Mockito.<Long>any());
    }
    @Test
    void cannotDeleteTransactionWithIncorrectUsername() {
        Transaction transaction = mock(Transaction.class);
        when(transaction.getUsername()).thenReturn("foo");
        doNothing().when(transaction).setCardNumber(Mockito.<String>any());
        doNothing().when(transaction).setMerchant(Mockito.<String>any());
        doNothing().when(transaction).setTransAmount(Mockito.<Double>any());
        doNothing().when(transaction).setTransCategory(Mockito.<String>any());
        doNothing().when(transaction).setTransDate(Mockito.<Date>any());
        doNothing().when(transaction).setTransID(Mockito.<Long>any());
        doNothing().when(transaction).setUsername(Mockito.<String>any());
        transaction.setCardNumber("42");
        transaction.setMerchant("Merchant");
        transaction.setTransAmount(10.0d);
        transaction.setTransCategory("Trans Category");
        transaction.setTransDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setTransID(1L);
        transaction.setUsername("janedoe");
        Optional<Transaction> ofResult = Optional.of(transaction);
        when(transactionRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(RequestException.class, () -> transactionService.deleteTransaction(1L, "janedoe"));
        verify(transaction).getUsername();
        verify(transaction).setCardNumber(Mockito.<String>any());
        verify(transaction).setMerchant(Mockito.<String>any());
        verify(transaction).setTransAmount(Mockito.<Double>any());
        verify(transaction).setTransCategory(Mockito.<String>any());
        verify(transaction).setTransDate(Mockito.<Date>any());
        verify(transaction).setTransID(Mockito.<Long>any());
        verify(transaction).setUsername(Mockito.<String>any());
        verify(transactionRepository).findById(Mockito.<Long>any());
    }

    @Test
    void editTransactionTest() {
        Transaction transaction = mock(Transaction.class);
        when(transaction.getMerchant()).thenThrow(new IllegalArgumentException("janedoe"));
        when(transaction.getUsername()).thenReturn("janedoe");
        doNothing().when(transaction).setCardNumber(Mockito.<String>any());
        doNothing().when(transaction).setMerchant(Mockito.<String>any());
        doNothing().when(transaction).setTransAmount(Mockito.<Double>any());
        doNothing().when(transaction).setTransCategory(Mockito.<String>any());
        doNothing().when(transaction).setTransDate(Mockito.<Date>any());
        doNothing().when(transaction).setTransID(Mockito.<Long>any());
        doNothing().when(transaction).setUsername(Mockito.<String>any());
        transaction.setCardNumber("42");
        transaction.setMerchant("Merchant");
        transaction.setTransAmount(10.0d);
        transaction.setTransCategory("Trans Category");
        transaction.setTransDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setTransID(1L);
        transaction.setUsername("janedoe");
        Optional<Transaction> ofResult = Optional.of(transaction);
        when(transactionRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(IllegalArgumentException.class,
                () -> transactionService.editTransaction("janedoe", 1L, "Merchant",
                        Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()), "Trans Category",
                        10.0d));
        verify(transaction).getMerchant();
        verify(transaction).getUsername();
        verify(transaction).setCardNumber(Mockito.<String>any());
        verify(transaction).setMerchant(Mockito.<String>any());
        verify(transaction).setTransAmount(Mockito.<Double>any());
        verify(transaction).setTransCategory(Mockito.<String>any());
        verify(transaction).setTransDate(Mockito.<Date>any());
        verify(transaction).setTransID(Mockito.<Long>any());
        verify(transaction).setUsername(Mockito.<String>any());
        verify(transactionRepository).findById(Mockito.<Long>any());
    }

    @Test
    void editTransactionTest2() {
        Transaction transaction = mock(Transaction.class);
        when(transaction.getTransAmount()).thenReturn(0.5d);
        when(transaction.getMerchant()).thenReturn("Merchant");
        when(transaction.getTransCategory()).thenReturn("Trans Category");
        when(transaction.getTransDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        when(transaction.getUsername()).thenReturn("janedoe");
        doNothing().when(transaction).setCardNumber(Mockito.<String>any());
        doNothing().when(transaction).setMerchant(Mockito.<String>any());
        doNothing().when(transaction).setTransAmount(Mockito.<Double>any());
        doNothing().when(transaction).setTransCategory(Mockito.<String>any());
        doNothing().when(transaction).setTransDate(Mockito.<Date>any());
        doNothing().when(transaction).setTransID(Mockito.<Long>any());
        doNothing().when(transaction).setUsername(Mockito.<String>any());
        transaction.setCardNumber("42");
        transaction.setMerchant("Merchant");
        transaction.setTransAmount(10.0d);
        transaction.setTransCategory("Trans Category");
        transaction.setTransDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setTransID(1L);
        transaction.setUsername("janedoe");
        Optional<Transaction> ofResult = Optional.of(transaction);
        when(transactionRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(creditCardService).updateBalance(Mockito.<Transaction>any());
        transactionService.editTransaction("janedoe", 1L, "Merchant",
                Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()), "Trans Category", 10.0d);
        verify(creditCardService, atLeast(1)).updateBalance(Mockito.<Transaction>any());
        verify(transaction).getMerchant();
        verify(transaction, atLeast(1)).getTransAmount();
        verify(transaction).getTransCategory();
        verify(transaction).getTransDate();
        verify(transaction).getUsername();
        verify(transaction).setCardNumber(Mockito.<String>any());
        verify(transaction).setMerchant(Mockito.<String>any());
        verify(transaction, atLeast(1)).setTransAmount(Mockito.<Double>any());
        verify(transaction).setTransCategory(Mockito.<String>any());
        verify(transaction).setTransDate(Mockito.<Date>any());
        verify(transaction).setTransID(Mockito.<Long>any());
        verify(transaction).setUsername(Mockito.<String>any());
        verify(transactionRepository).findById(Mockito.<Long>any());
    }

    @Test
    void editTransactionTest3() {
        Transaction transaction = mock(Transaction.class);
        when(transaction.getUsername()).thenReturn("foo");
        doNothing().when(transaction).setCardNumber(Mockito.<String>any());
        doNothing().when(transaction).setMerchant(Mockito.<String>any());
        doNothing().when(transaction).setTransAmount(Mockito.<Double>any());
        doNothing().when(transaction).setTransCategory(Mockito.<String>any());
        doNothing().when(transaction).setTransDate(Mockito.<Date>any());
        doNothing().when(transaction).setTransID(Mockito.<Long>any());
        doNothing().when(transaction).setUsername(Mockito.<String>any());
        transaction.setCardNumber("42");
        transaction.setMerchant("Merchant");
        transaction.setTransAmount(10.0d);
        transaction.setTransCategory("Trans Category");
        transaction.setTransDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setTransID(1L);
        transaction.setUsername("janedoe");
        Optional<Transaction> ofResult = Optional.of(transaction);
        when(transactionRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(RequestException.class,
                () -> transactionService.editTransaction("janedoe", 1L, "Merchant",
                        Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()), "Trans Category",
                        10.0d));
        verify(transaction).getUsername();
        verify(transaction).setCardNumber(Mockito.<String>any());
        verify(transaction).setMerchant(Mockito.<String>any());
        verify(transaction).setTransAmount(Mockito.<Double>any());
        verify(transaction).setTransCategory(Mockito.<String>any());
        verify(transaction).setTransDate(Mockito.<Date>any());
        verify(transaction).setTransID(Mockito.<Long>any());
        verify(transaction).setUsername(Mockito.<String>any());
        verify(transactionRepository).findById(Mockito.<Long>any());
    }

    @Test
    void getTotalTransactionAmountInMonthsTest() {
        when(transactionRepository.findTransactionByMonth(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any(), Mockito.<Integer>any())).thenReturn(new ArrayList<>());
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        List<Double> actualTotalTransactionAmountInMonths = transactionService.getTotalTransactionAmountInMonths("janedoe",
                "42", 1);
        verify(transactionRepository, atLeast(1)).findTransactionByMonth(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any(), Mockito.<Integer>any());
        verify(customerRepository, atLeast(1)).existsById(Mockito.<String>any());
        assertEquals(0.0d, actualTotalTransactionAmountInMonths.get(0));
        assertEquals(0.0d, actualTotalTransactionAmountInMonths.get(1));
        assertEquals(0.0d, actualTotalTransactionAmountInMonths.get(10));
        assertEquals(0.0d, actualTotalTransactionAmountInMonths.get(11));
        assertEquals(0.0d, actualTotalTransactionAmountInMonths.get(2));
        assertEquals(0.0d, actualTotalTransactionAmountInMonths.get(3));
        assertEquals(0.0d, actualTotalTransactionAmountInMonths.get(4));
        assertEquals(0.0d, actualTotalTransactionAmountInMonths.get(5));
        assertEquals(0.0d, actualTotalTransactionAmountInMonths.get(6));
        assertEquals(0.0d, actualTotalTransactionAmountInMonths.get(7));
        assertEquals(0.0d, actualTotalTransactionAmountInMonths.get(8));
        assertEquals(0.0d, actualTotalTransactionAmountInMonths.get(9));
        assertEquals(12, actualTotalTransactionAmountInMonths.size());
    }

    @Test
    void getTotalTransactionAmountInMonthsTest2() {
        Transaction transaction = new Transaction();
        transaction.setCardNumber("42");
        transaction.setMerchant("Merchant");
        transaction.setTransAmount(10.0d);
        transaction.setTransCategory("Trans Category");
        transaction.setTransDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setTransID(1L);
        transaction.setUsername("janedoe");

        ArrayList<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        when(transactionRepository.findTransactionByMonth(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any(), Mockito.<Integer>any())).thenReturn(transactionList);
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        List<Double> actualTotalTransactionAmountInMonths = transactionService.getTotalTransactionAmountInMonths("janedoe",
                "42", 1);
        verify(transactionRepository, atLeast(1)).findTransactionByMonth(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any(), Mockito.<Integer>any());
        verify(customerRepository, atLeast(1)).existsById(Mockito.<String>any());
        assertEquals(10.0d, actualTotalTransactionAmountInMonths.get(0));
        assertEquals(10.0d, actualTotalTransactionAmountInMonths.get(1));
        assertEquals(10.0d, actualTotalTransactionAmountInMonths.get(10));
        assertEquals(10.0d, actualTotalTransactionAmountInMonths.get(11));
        assertEquals(10.0d, actualTotalTransactionAmountInMonths.get(2));
        assertEquals(10.0d, actualTotalTransactionAmountInMonths.get(3));
        assertEquals(10.0d, actualTotalTransactionAmountInMonths.get(4));
        assertEquals(10.0d, actualTotalTransactionAmountInMonths.get(5));
        assertEquals(10.0d, actualTotalTransactionAmountInMonths.get(6));
        assertEquals(10.0d, actualTotalTransactionAmountInMonths.get(7));
        assertEquals(10.0d, actualTotalTransactionAmountInMonths.get(8));
        assertEquals(10.0d, actualTotalTransactionAmountInMonths.get(9));
        assertEquals(12, actualTotalTransactionAmountInMonths.size());
    }

    @Test
    void getTotalTransactionAmountInDaysTest() {
        when(transactionRepository.findTransactionByDate(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Integer>any())).thenReturn(new ArrayList<>());
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        List<Pair<Integer, Double>> actualTotalTransactionAmountInDays = transactionService
                .getTotalTransactionAmountInDays("janedoe", "42", 1, 1);
        verify(transactionRepository, atLeast(1)).findTransactionByDate(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Integer>any());
        verify(customerRepository, atLeast(1)).existsById(Mockito.<String>any());
        Pair<Integer, Double> getResult = actualTotalTransactionAmountInDays.get(0);
        assertEquals(0, getResult.getFirst().intValue());
        assertEquals(0.0d, getResult.getSecond().doubleValue());
        Pair<Integer, Double> getResult2 = actualTotalTransactionAmountInDays.get(1);
        assertEquals(0.0d, getResult2.getSecond().doubleValue());
        Pair<Integer, Double> getResult3 = actualTotalTransactionAmountInDays.get(2);
        assertEquals(0.0d, getResult3.getSecond().doubleValue());
        Pair<Integer, Double> getResult4 = actualTotalTransactionAmountInDays.get(25);
        assertEquals(0.0d, getResult4.getSecond().doubleValue());
        Pair<Integer, Double> getResult5 = actualTotalTransactionAmountInDays.get(26);
        assertEquals(0.0d, getResult5.getSecond().doubleValue());
        Pair<Integer, Double> getResult6 = actualTotalTransactionAmountInDays.get(27);
        assertEquals(0.0d, getResult6.getSecond().doubleValue());
        Pair<Integer, Double> getResult7 = actualTotalTransactionAmountInDays.get(28);
        assertEquals(0.0d, getResult7.getSecond().doubleValue());
        Pair<Integer, Double> getResult8 = actualTotalTransactionAmountInDays.get(29);
        assertEquals(0.0d, getResult8.getSecond().doubleValue());
        Pair<Integer, Double> getResult9 = actualTotalTransactionAmountInDays.get(3);
        assertEquals(0.0d, getResult9.getSecond().doubleValue());
        Pair<Integer, Double> getResult10 = actualTotalTransactionAmountInDays.get(30);
        assertEquals(0.0d, getResult10.getSecond().doubleValue());
        Pair<Integer, Double> getResult11 = actualTotalTransactionAmountInDays.get(4);
        assertEquals(0.0d, getResult11.getSecond().doubleValue());
        Pair<Integer, Double> getResult12 = actualTotalTransactionAmountInDays.get(5);
        assertEquals(0.0d, getResult12.getSecond().doubleValue());
        assertEquals(1, getResult2.getFirst().intValue());
        assertEquals(2, getResult3.getFirst().intValue());
        assertEquals(25, getResult4.getFirst().intValue());
        assertEquals(26, getResult5.getFirst().intValue());
        assertEquals(27, getResult6.getFirst().intValue());
        assertEquals(28, getResult7.getFirst().intValue());
        assertEquals(29, getResult8.getFirst().intValue());
        assertEquals(3, getResult9.getFirst().intValue());
        assertEquals(30, getResult10.getFirst().intValue());
        assertEquals(31, actualTotalTransactionAmountInDays.size());
        assertEquals(4, getResult11.getFirst().intValue());
        assertEquals(5, getResult12.getFirst().intValue());
    }

    @Test
    void getTotalTransactionAmountInDaysTest2() {
        when(transactionRepository.findTransactionByDate(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Integer>any()))
                .thenThrow(new SuccessfulRequest("An error occurred"));
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        assertThrows(SuccessfulRequest.class,
                () -> transactionService.getTotalTransactionAmountInDays("janedoe", "42", 1, 1));
        verify(transactionRepository).findTransactionByDate(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Integer>any());
        verify(customerRepository).existsById(Mockito.<String>any());
    }
    @Test
    void getTotalTransactionAmountInDaysTest3() {
        Transaction transaction = new Transaction();
        transaction.setCardNumber("42");
        transaction.setMerchant("Merchant");
        transaction.setTransAmount(10.0d);
        transaction.setTransCategory("Trans Category");
        transaction.setTransDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setTransID(1L);
        transaction.setUsername("janedoe");

        ArrayList<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        when(transactionRepository.findTransactionByDate(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Integer>any())).thenReturn(transactionList);
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        List<Pair<Integer, Double>> actualTotalTransactionAmountInDays = transactionService
                .getTotalTransactionAmountInDays("janedoe", "42", 1, 1);
        verify(transactionRepository, atLeast(1)).findTransactionByDate(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Integer>any());
        verify(customerRepository, atLeast(1)).existsById(Mockito.<String>any());
        Pair<Integer, Double> getResult = actualTotalTransactionAmountInDays.get(0);
        assertEquals(0, getResult.getFirst().intValue());
        Pair<Integer, Double> getResult2 = actualTotalTransactionAmountInDays.get(1);
        assertEquals(1, getResult2.getFirst().intValue());
        assertEquals(10.0d, getResult.getSecond().doubleValue());
        assertEquals(10.0d, getResult2.getSecond().doubleValue());
        Pair<Integer, Double> getResult3 = actualTotalTransactionAmountInDays.get(2);
        assertEquals(10.0d, getResult3.getSecond().doubleValue());
        Pair<Integer, Double> getResult4 = actualTotalTransactionAmountInDays.get(25);
        assertEquals(10.0d, getResult4.getSecond().doubleValue());
        Pair<Integer, Double> getResult5 = actualTotalTransactionAmountInDays.get(26);
        assertEquals(10.0d, getResult5.getSecond().doubleValue());
        Pair<Integer, Double> getResult6 = actualTotalTransactionAmountInDays.get(27);
        assertEquals(10.0d, getResult6.getSecond().doubleValue());
        Pair<Integer, Double> getResult7 = actualTotalTransactionAmountInDays.get(28);
        assertEquals(10.0d, getResult7.getSecond().doubleValue());
        Pair<Integer, Double> getResult8 = actualTotalTransactionAmountInDays.get(29);
        assertEquals(10.0d, getResult8.getSecond().doubleValue());
        Pair<Integer, Double> getResult9 = actualTotalTransactionAmountInDays.get(3);
        assertEquals(10.0d, getResult9.getSecond().doubleValue());
        Pair<Integer, Double> getResult10 = actualTotalTransactionAmountInDays.get(30);
        assertEquals(10.0d, getResult10.getSecond().doubleValue());
        Pair<Integer, Double> getResult11 = actualTotalTransactionAmountInDays.get(4);
        assertEquals(10.0d, getResult11.getSecond().doubleValue());
        Pair<Integer, Double> getResult12 = actualTotalTransactionAmountInDays.get(5);
        assertEquals(10.0d, getResult12.getSecond().doubleValue());
        assertEquals(2, getResult3.getFirst().intValue());
        assertEquals(25, getResult4.getFirst().intValue());
        assertEquals(26, getResult5.getFirst().intValue());
        assertEquals(27, getResult6.getFirst().intValue());
        assertEquals(28, getResult7.getFirst().intValue());
        assertEquals(29, getResult8.getFirst().intValue());
        assertEquals(3, getResult9.getFirst().intValue());
        assertEquals(30, getResult10.getFirst().intValue());
        assertEquals(31, actualTotalTransactionAmountInDays.size());
        assertEquals(4, getResult11.getFirst().intValue());
        assertEquals(5, getResult12.getFirst().intValue());
    }

    @Test
    void testGetBalanceInMonths() {
        Optional<Double> ofResult = Optional.<Double>of(10.0d);
        when(transactionRepository.findBalanceUpToDate(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<ZonedDateTime>any())).thenReturn(ofResult);
        List<Double> actualBalanceInMonths = transactionService.getBalanceInMonths("janedoe", "42", 10);
        verify(transactionRepository, atLeast(1)).findBalanceUpToDate(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<ZonedDateTime>any());
        assertEquals(10.0d, actualBalanceInMonths.get(0));
        assertEquals(10.0d, actualBalanceInMonths.get(1));
        assertEquals(10.0d, actualBalanceInMonths.get(10));
        assertEquals(10.0d, actualBalanceInMonths.get(2));
        assertEquals(10.0d, actualBalanceInMonths.get(3));
        assertEquals(10.0d, actualBalanceInMonths.get(4));
        assertEquals(10.0d, actualBalanceInMonths.get(5));
        assertEquals(10.0d, actualBalanceInMonths.get(6));
        assertEquals(10.0d, actualBalanceInMonths.get(7));
        assertEquals(10.0d, actualBalanceInMonths.get(8));
        assertEquals(10.0d, actualBalanceInMonths.get(9));
        assertEquals(11, actualBalanceInMonths.size());
    }
    @Test
    void testGetBalanceInDays() {
        Optional<Double> ofResult = Optional.<Double>of(10.0d);
        when(transactionRepository.findBalanceUpToDate(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<ZonedDateTime>any())).thenReturn(ofResult);
        List<Double> actualBalanceInDays = transactionService.getBalanceInDays("janedoe", "42", 10);
        verify(transactionRepository, atLeast(1)).findBalanceUpToDate(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<ZonedDateTime>any());
        assertEquals(10.0d, actualBalanceInDays.get(0));
        assertEquals(10.0d, actualBalanceInDays.get(1));
        assertEquals(10.0d, actualBalanceInDays.get(10));
        assertEquals(10.0d, actualBalanceInDays.get(2));
        assertEquals(10.0d, actualBalanceInDays.get(3));
        assertEquals(10.0d, actualBalanceInDays.get(4));
        assertEquals(10.0d, actualBalanceInDays.get(5));
        assertEquals(10.0d, actualBalanceInDays.get(6));
        assertEquals(10.0d, actualBalanceInDays.get(7));
        assertEquals(10.0d, actualBalanceInDays.get(8));
        assertEquals(10.0d, actualBalanceInDays.get(9));
        assertEquals(11, actualBalanceInDays.size());
    }
    @Test
    void doesCreditCardExistTest() {
        CreditCard creditCard = new CreditCard();
        creditCard.setAvailableCredit(10.0d);
        creditCard.setCardNumber("42");
        creditCard.setCreditLimit(10.0d);
        creditCard.setCurrentBalance(10.0d);
        creditCard.setDueDate(mock(java.sql.Date.class));
        creditCard.setInterestRate(10.0d);
        creditCard.setIssuer("Issuer");
        creditCard.setMinimumPayment(10.0d);
        creditCard.setPaymentDue(10.0d);
        creditCard.setUsername("janedoe");
        when(creditCardService.doesCreditCardExist(Mockito.<String>any())).thenReturn(creditCard);
        transactionService.doesCreditCardExist("42");
        verify(creditCardService).doesCreditCardExist(Mockito.<String>any());
    }
    @Test
    void updateCreditCardBalanceTest() {
        doNothing().when(creditCardService).updateBalance(Mockito.<Transaction>any());

        Transaction transaction = new Transaction();
        transaction.setCardNumber("42");
        transaction.setMerchant("Merchant");
        transaction.setTransAmount(10.0d);
        transaction.setTransCategory("Trans Category");
        transaction.setTransDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setTransID(1L);
        transaction.setUsername("janedoe");
        transactionService.updateCreditCardBalance(transaction);
        verify(creditCardService).updateBalance(Mockito.<Transaction>any());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("1969-12-31", simpleDateFormat.format(transaction.getTransDate()));
        assertEquals("42", transaction.getCardNumber());
        assertEquals("Merchant", transaction.getMerchant());
        assertEquals("Trans Category", transaction.getTransCategory());
        assertEquals("janedoe", transaction.getUsername());
        assertEquals(10.0d, transaction.getTransAmount().doubleValue());
        assertEquals(1L, transaction.getTransID().longValue());
    }

}
