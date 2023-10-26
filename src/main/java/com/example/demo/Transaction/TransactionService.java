package com.example.demo.Transaction;

import com.example.demo.Customer.Customer;
import com.example.demo.Customer.CustomerRepository;
import com.example.demo.Exception.RequestException;
import com.example.demo.Exception.SuccessfulRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;


    @Autowired
    public TransactionService (TransactionRepository transactionRepository,
                               CustomerRepository customerRepository){
        this.transactionRepository=transactionRepository;
        this.customerRepository = customerRepository;
    }

    public boolean isUsernamePresent(String username){
        Optional <Customer> CustomerOptionalUsername = customerRepository
                .findUsernameCustomer(username);
        return CustomerOptionalUsername.isPresent();
    }

    public void addNewTransaction (Transaction transaction, String username) {
        if (isUsernamePresent(username)){
            transaction.setUsername(username);
            if(transaction.getMerchant()==null)
                transaction.setMerchant("Unknown");
            transactionRepository.save(transaction);
        }
        else
            throw new RequestException("This username does not exist");
    }

    public List<Transaction> getTransaction(String username){
        if (isUsernamePresent(username))
            return transactionRepository
                .findTransactionByUsername(username);
        else
            throw new RequestException("This username does not exist");
    }

    public List<Transaction> getTransactionByCategory(String username, String transCategory){
        if (isUsernamePresent(username)) {
            if (transactionRepository
                    .findTransactionByCategory(username, transCategory).isEmpty())
                throw new SuccessfulRequest("There is no such transaction");
            else
                return transactionRepository
                    .findTransactionByCategory(username, transCategory);
        }
        else
            throw new RequestException("This username does not exist");
    }

    public List<Transaction> getTransactionByMerchant(String username, String merchant){
        if (isUsernamePresent(username)) {
            if (transactionRepository
                    .findTransactionByMerchant(username, merchant).isEmpty())
                throw new SuccessfulRequest("There is no such transaction");
            else
                return transactionRepository
                        .findTransactionByMerchant(username, merchant);
        }
        else
            throw new RequestException("This username does not exist");
    }
    public List<Transaction> getTransactionByYear(String username, Integer year) {
        if (isUsernamePresent(username)) {
            if (transactionRepository.findTransactionByYear(username, year).isEmpty())
                throw new SuccessfulRequest("There is no such transaction");
            else
                return transactionRepository
                        .findTransactionByYear(username, year);
        }
        else
            throw new RequestException("This username does not exist");
    }
    public List<Transaction> getTransactionByMonth(String username, Integer month) {
        if (isUsernamePresent(username)) {
            if (transactionRepository.findTransactionByMonth(username, month).isEmpty())
                throw new SuccessfulRequest("There is no such transaction");
            else
                return transactionRepository
                        .findTransactionByMonth(username, month);
        }
        else
            throw new RequestException("This username does not exist");
    }

    public List<Transaction> getTransactionByCardNumber(String username, String cardNumber) {
        if (isUsernamePresent(username)) {
            if (transactionRepository.findTransactionByCardNumber(username, cardNumber).isEmpty())
                throw new SuccessfulRequest("There is no such transaction");
            else
                return transactionRepository
                        .findTransactionByCardNumber(username, cardNumber);
        }
        else
            throw new RequestException("This username does not exist");
    }

    public void deleteTransaction (Long transID, String username){
        Transaction transaction = transactionRepository.findById(transID)
                .orElseThrow(()-> new RequestException(
                        "This transaction does not exist"
                ));
        if (transaction.getUsername().equals(username))
            transactionRepository.deleteById(transID);
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
                transaction.setTransAmount(transAmount);
            }
            if (transDate != null && !Objects.equals(transaction.getTransDate(), transDate)) {
                transaction.setTransDate(transDate);
            }
        }
        else
            throw new RequestException( "This transaction does not exist" );
    }


}
