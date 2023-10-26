package com.example.demo.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path ="transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService){
        this.transactionService=transactionService;
    }


    // Add a new transaction
    @PostMapping(path="{username}/new")
    public void addNewTransaction(@PathVariable ("username")String username,@RequestBody Transaction transaction){
        transactionService.addNewTransaction(transaction, username);
    }

    // display all transactions of a username
   @GetMapping(path ="{username}")
    public List<Transaction> getTransaction(@PathVariable("username") String username){
        return transactionService.getTransaction(username);
    }

    // display all transactions of a username filter by Category
    @GetMapping(path ="{username}/category={transCategory}")
    public List<Transaction> getTransactionByCategory(@PathVariable("username") String username,
                                            @PathVariable("transCategory") String transCategory){
        return transactionService.getTransactionByCategory(username,transCategory);
    }

    // display all transactions of a username filter by Merchant
    @GetMapping(path ="{username}/merchant={merchant}")
    public List<Transaction> getTransactionByMerchant(@PathVariable("username") String username,
                                                      @PathVariable("merchant") String merchant){
        return transactionService.getTransactionByMerchant(username, merchant);
    }

    //display all transactions of a username by year
    @GetMapping(path ="{username}/year={year}")
    public List<Transaction> getTransactionByYear(@PathVariable("username") String username,
                                                      @PathVariable("year") Integer year){
        return transactionService.getTransactionByYear(username, year);
    }

    //display all transactions of a username by month
    @GetMapping(path ="{username}/month={month}")
    public List<Transaction> getTransactionByMonth(@PathVariable("username") String username,
                                                  @PathVariable("month") Integer month){
        return transactionService.getTransactionByMonth(username, month);
    }

    // display all transactions by card number
    @GetMapping(path="{username}/card={cardNumber}")
    public List <Transaction> getTransactionByCardNumber (@PathVariable("username") String username,
                                                          @PathVariable("cardNumber") String cardNumber){
        return transactionService.getTransactionByCardNumber(username, cardNumber);
    }

    @DeleteMapping(path="{username}/delete/id={transID}")
    public void deleteTransaction(@PathVariable("username") String username, @PathVariable("transID") Long transID){
        transactionService.deleteTransaction(transID, username);
    }

    @PutMapping(path ="{username}/editing/id={transID}")
    public void editTransaction(
            @PathVariable("username") String username,
            @PathVariable("transID") Long transID,
            @RequestParam(required = false) String merchant,
            @RequestParam(required = false) Date transDate,
            @RequestParam(required = false) String transCategory,
            @RequestParam(required = false) Double transAmount
    ){
        transactionService.editTransaction(username, transID, merchant, transDate, transCategory, transAmount);
    }

}
