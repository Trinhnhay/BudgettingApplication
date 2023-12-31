package com.example.demo.Transaction;

import com.example.demo.CreditCard.CreditCardService;
import com.example.demo.Customer.CustomerRepository;
import com.example.demo.Exception.RequestException;
import com.example.demo.Exception.SuccessfulRequest;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final CreditCardService creditCardService;

    @Autowired
    public TransactionService (TransactionRepository transactionRepository,
                               CustomerRepository customerRepository,
                               CreditCardService creditCardService){
        this.transactionRepository=transactionRepository;
        this.customerRepository = customerRepository;
        this.creditCardService = creditCardService;
    }

    public void addNewTransaction (@NotNull Transaction transaction, String username) {
        doesUsernameExist(username);
        transaction.setUsername(username);
        doesCreditCardExist(transaction.getCardNumber());
        transactionRepository.save(transaction);
        updateCreditCardBalance(transaction);
    }


    public List<Transaction> getTransaction(String username){
        doesUsernameExist(username);
        return transactionRepository
                .findTransactionByUsername(username);
    }

    public List<Transaction> getTransactionByCategory(String username, String transCategory){
        doesUsernameExist(username);
        if (transactionRepository
                .findTransactionByCategory(username, transCategory).isEmpty())
            throw new SuccessfulRequest("There is no such transaction");
        else
            return transactionRepository
                    .findTransactionByCategory(username, transCategory);
    }

    public List<Transaction> getTransactionByMerchant(String username, String merchant){
        doesUsernameExist(username);
        if (transactionRepository
                .findTransactionByMerchant(username, merchant).isEmpty())
            throw new SuccessfulRequest("There is no such transaction");
        else
            return transactionRepository
                    .findTransactionByMerchant(username, merchant);
    }

    public List<Transaction> getTransactionByYear(String username, String cardNumber, Integer year) {
        doesUsernameExist(username);
        if (transactionRepository.findTransactionByYear(username,cardNumber, year).isEmpty())
            throw new SuccessfulRequest("There is no such transaction");
        else
            return transactionRepository
                    .findTransactionByYear(username,cardNumber, year);
    }
    public List<Transaction> getTransactionByMonth(String username, String cardNumber, Integer month, Integer year) {
        doesUsernameExist(username);
        return transactionRepository
                .findTransactionByMonth(username, cardNumber, month, year);
    }

    public List<Transaction> getTransactionByDay(String username, String cardNumber, Integer month, Integer year, Integer day){
        doesUsernameExist(username);
        return transactionRepository
                .findTransactionByDate(username, cardNumber, month, year, day);
    }
    public List<Transaction> getTransactionByCardNumber(String username, String cardNumber) {
        doesUsernameExist(username);
        if (transactionRepository.findTransactionByCardNumber(username, cardNumber).isEmpty())
            throw new SuccessfulRequest("There is no such transaction");
        else
            return transactionRepository
                    .findTransactionByCardNumber(username, cardNumber);
    }

    public void deleteTransaction (Long transID, String username){
        Transaction transaction = transactionRepository.findById(transID)
                .orElseThrow(()-> new RequestException(
                        "This transaction does not exist"
                ));
        if (transaction.getUsername().equals(username)) {
            transaction.setTransAmount(-transaction.getTransAmount());
            creditCardService.updateBalance(transaction);
            transactionRepository.deleteById(transID);
        }
        else
            throw new RequestException("This transaction does not exist");
    }

    @Transactional
    public void editTransaction(String username, Long transID, String merchant, Date transDate, String transCategory, Double transAmount){
        Transaction transaction = transactionRepository.findById(transID)
                .orElseThrow(()-> new RequestException(
                        "This transaction does not exist"
                ));
        if (transaction.getUsername().equals(username)) {
            if (merchant != null && !merchant.isEmpty() && !Objects.equals(transaction.getMerchant(), merchant)) {
                transaction.setMerchant(merchant);
            }
            if (transCategory != null && !transCategory.isEmpty() && !Objects.equals(transaction.getTransCategory(), transCategory)) {
                transaction.setTransCategory(transCategory);
            }
            if (transAmount != null && !Objects.equals(transaction.getTransAmount(), transAmount)) {
                transaction.setTransAmount(-transaction.getTransAmount());
                creditCardService.updateBalance(transaction);
                transaction.setTransAmount(transAmount);
                creditCardService.updateBalance(transaction);
            }
            if (transDate != null && !Objects.equals(transaction.getTransDate(), transDate)) {
                transaction.setTransDate(transDate);
            }
        }
        else
            throw new RequestException( "This transaction does not exist" );
    }

    public List <Double> getTotalTransactionAmountInMonths(String username, String cardNumber, Integer year){
        List <Double> totalAmountInMonths = new ArrayList<>();
        List <Transaction> allTransactionInMonth ;
        for (int i=0; i<12; i++){
            allTransactionInMonth = getTransactionByMonth(username, cardNumber,i+1,year);
            if (allTransactionInMonth.isEmpty())
                totalAmountInMonths.add(0.0);
            else {
                Double amount = 0.0;
                for (Transaction transaction : allTransactionInMonth) {
                    amount += transaction.getTransAmount();
                }
                totalAmountInMonths.add(amount);
            }
        }

        return totalAmountInMonths;
    }

    public List<Pair <Integer,Double>> getTotalTransactionAmountInDays (String username, String cardNumber, Integer month, Integer year){
        Pair<String,Double> pair = null;
        List <Pair <Integer,Double> > totalAmountInDays = new ArrayList<>();
        List <Transaction> allTransactionInDay;
        int day = 30;

        if (month==1 ||month==3 || month==5 || month==7 || month==8 || month==10 || month==12 )
            day =31;
        else if (month ==2){
            if (year%4==0)
             day = 29;
            else day = 28;
        }
        for (int i=0; i<day; i++){
            allTransactionInDay = getTransactionByDay(username, cardNumber,month,year,i+1);
            if (allTransactionInDay.isEmpty())
                totalAmountInDays.add(pair.of(i,0.0));
            else {
                Double amount = 0.0;
                for (Transaction transaction : allTransactionInDay) {
                    amount += transaction.getTransAmount();
                }
                totalAmountInDays.add(pair.of(i,amount));
            }
        }
        return totalAmountInDays;
    }

    public List<Double> getBalanceInMonths(String username, String cardNumber, Integer numOfMonths) {
        ZonedDateTime currentDate = ZonedDateTime.now();
        ZonedDateTime farthestDate = currentDate.minusMonths(numOfMonths);
        List<Double> monthBalances = new ArrayList<>();
        for (int i = 0; i < numOfMonths; i++) {
            Optional<Double> bal = transactionRepository.findBalanceUpToDate(username, cardNumber, farthestDate);
            if (bal.isPresent()) {
                monthBalances.add(bal.get());
            }
            farthestDate = farthestDate.plusMonths(1);
        }
        Optional<Double> currentBalance = transactionRepository.findBalanceUpToDate(username, cardNumber, currentDate);
        if (currentBalance.isPresent()) {
            monthBalances.add(currentBalance.get());
        }
        return monthBalances;
    }

    public List<Double> getBalanceInDays(String username, String cardNumber, Integer numOfDays) {
        ZonedDateTime currentDate = ZonedDateTime.now();
        ZonedDateTime farthestDate = currentDate.minusDays(numOfDays);
        List<Double> dayBalances = new ArrayList<>();
        for (int i = 0; i < numOfDays; i++) {
            Optional<Double> bal = transactionRepository.findBalanceUpToDate(username, cardNumber, farthestDate);
            if (bal.isPresent()) {
                dayBalances.add(bal.get());
            }
            farthestDate = farthestDate.plusDays(1);
        }
        Optional<Double> currentBalance = transactionRepository.findBalanceUpToDate(username, cardNumber, currentDate);
        if (currentBalance.isPresent()) {
            dayBalances.add(currentBalance.get());
        }
        return dayBalances;
    }

    public void doesUsernameExist(String username) {
        if (!customerRepository.existsById(username))
            throw new RequestException("This username does not exist");
    }
    public void doesCreditCardExist(String cardNumber) {
        creditCardService.doesCreditCardExist(cardNumber);
    }
    public void updateCreditCardBalance(Transaction transaction){
        creditCardService.updateBalance(transaction);
    }
}
