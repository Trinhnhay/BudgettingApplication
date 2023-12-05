package com.example.demo.Transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.CreditCard.CreditCardRepository;
import com.example.demo.CreditCard.CreditCardService;
import com.example.demo.Customer.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {TransactionController.class})
@ExtendWith(SpringExtension.class)
class TransactionControllerTest {
    @Autowired
    private TransactionController transactionController;

    @MockBean
    private TransactionService transactionService;

    @Test
    void addNewTransactionTest() throws Exception {
        doNothing().when(transactionService).addNewTransaction(Mockito.<Transaction>any(), Mockito.<String>any());

        Transaction transaction = new Transaction();
        transaction.setCardNumber("42");
        transaction.setMerchant("Merchant");
        transaction.setTransAmount(10.0d);
        transaction.setTransCategory("Trans Category");
        transaction.setTransDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setTransID(1L);
        transaction.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(transaction);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/transactions/{username}/new", "janedoe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getTransactionTest() throws Exception {
        when(transactionService.getTransaction(Mockito.<String>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/transactions/{username}", "janedoe");
        MockMvcBuilders.standaloneSetup(transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void getTransactionByCategoryTest() throws Exception {
        when(transactionService.getTransactionByCategory(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/transactions/{username}/category={transCategory}", "janedoe", "Trans Category");
        MockMvcBuilders.standaloneSetup(transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void getTransactionByMerchantTest() throws Exception {
        when(transactionService.getTransactionByMerchant(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/transactions/{username}/merchant={merchant}", "janedoe", "Merchant");
        MockMvcBuilders.standaloneSetup(transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void getTransactionByYearTest() throws Exception {
        when(transactionService.getTransactionByYear(Mockito.<String>any(), Mockito.<String>any(), Mockito.<Integer>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/transactions/{username}/card={cardNumber}/year={year}", "janedoe", "42", 1);
        MockMvcBuilders.standaloneSetup(transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void getTransactionByMonthTest() throws Exception {
        when(transactionService.getTransactionByMonth(Mockito.<String>any(), Mockito.<String>any(), Mockito.<Integer>any(),
                Mockito.<Integer>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/transactions/{username}/{cardNumber}/month={month}/year={year}", "janedoe", "42", 1, 1);
        MockMvcBuilders.standaloneSetup(transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void getTransactionByDayTest() throws Exception {
        when(transactionService.getTransactionByDay(Mockito.<String>any(), Mockito.<String>any(), Mockito.<Integer>any(),
                Mockito.<Integer>any(), Mockito.<Integer>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/transactions/{username}/{cardNumber}/day={day}/month={month}/year={year}", "janedoe", "42", 1, 1, 1);
        MockMvcBuilders.standaloneSetup(transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void getTransactionByCardNumberTest() throws Exception {
        when(transactionService.getTransactionByCardNumber(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/transactions/{username}/card={cardNumber}", "janedoe", "42");
        MockMvcBuilders.standaloneSetup(transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
    @Test
    void deleteTransactionTest() throws Exception {
        doNothing().when(transactionService).deleteTransaction(Mockito.<Long>any(), Mockito.<String>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/transactions/{username}/delete/id={transID}", "janedoe", 1L);
        MockMvcBuilders.standaloneSetup(transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void editTransactionTest() throws Exception {
        doNothing().when(transactionService)
                .editTransaction(Mockito.<String>any(), Mockito.<Long>any(), Mockito.<String>any(), Mockito.<Date>any(),
                        Mockito.<String>any(), Mockito.<Double>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/transactions/{username}/editing/id={transID}", "janedoe", 1L);
        MockMvcBuilders.standaloneSetup(transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void getTotalTransactionAmountInMonthsTest() throws Exception {
        when(transactionService.getTotalTransactionAmountInMonths(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/transactions/{username}/amount-in-months-of-{cardNumber}-in-{year}", "janedoe", "42", 1);
        MockMvcBuilders.standaloneSetup(transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void getTotalTransactionAmountInDaysTest() {
        TransactionRepository transactionRepository = mock(TransactionRepository.class);
        when(transactionRepository.findTransactionByDate(Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<Integer>any(), Mockito.<Integer>any(), Mockito.<Integer>any())).thenReturn(new ArrayList<>());
        CustomerRepository customerRepository = mock(CustomerRepository.class);
        when(customerRepository.existsById(Mockito.<String>any())).thenReturn(true);
        List<Pair<Integer, Double>> actualTotalTransactionAmountInDays = (new TransactionController(
                new TransactionService(transactionRepository, customerRepository,
                        new CreditCardService(mock(CreditCardRepository.class), mock(CustomerRepository.class)))))
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
    void getBalanceInMonthsTest() throws Exception {
        when(transactionService.getBalanceInMonths(Mockito.<String>any(), Mockito.<String>any(), Mockito.<Integer>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders
                .get("/transactions/{username}/balance-in-months-of-{cardNumber}", "janedoe", "42");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("numOfMonths", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void getBalanceInDaysTest() throws Exception {
        when(transactionService.getBalanceInDays(Mockito.<String>any(), Mockito.<String>any(), Mockito.<Integer>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders
                .get("/transactions/{username}/balance-in-days-of-{cardNumber}", "janedoe", "42");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("numOfDays", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
