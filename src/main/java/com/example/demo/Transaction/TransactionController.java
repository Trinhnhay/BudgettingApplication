package com.example.demo.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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
    public void addNewTransaction(@PathVariable ("username")String username,
                                  @RequestBody Transaction transaction){
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

    //display all username's transactions of a card by year
    @GetMapping(path ="{username}/card={cardNumber}/year={year}")
    public List<Transaction> getTransactionByYear(@PathVariable("username") String username,
                                                  @PathVariable("cardNumber") String cardNumber,
                                                      @PathVariable("year") Integer year){
        return transactionService.getTransactionByYear(username, cardNumber, year);
    }

    //display all transactions of a username by month
    @GetMapping(path ="{username}/{cardNumber}/month={month}/year={year}")
    public List<Transaction> getTransactionByMonth(@PathVariable("username") String username,
                                                   @PathVariable("cardNumber") String cardNumber,
                                                  @PathVariable("month") Integer month,
                                                   @PathVariable("year") Integer year){
        return transactionService.getTransactionByMonth(username,cardNumber, month, year);
    }
    @GetMapping(path ="{username}/{cardNumber}/day={day}/month={month}/year={year}")
    public List<Transaction> getTransactionByDay(@PathVariable("username") String username,
                                                   @PathVariable("cardNumber") String cardNumber,
                                                    @PathVariable("day") Integer day,
                                                   @PathVariable("month") Integer month,
                                                   @PathVariable("year") Integer year){
        return transactionService.getTransactionByDay(username,cardNumber, month, year, day);
    }

    // display all transactions by card number
    @GetMapping(path="{username}/card={cardNumber}")
    public List <Transaction> getTransactionByCardNumber (@PathVariable("username") String username,
                                                          @PathVariable("cardNumber") String cardNumber){
        return transactionService.getTransactionByCardNumber(username, cardNumber);
    }

    @DeleteMapping(path="{username}/delete/id={transID}")
    public void deleteTransaction(@PathVariable("username") String username,
                                  @PathVariable("transID") Long transID){
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

    @GetMapping (path ="{username}/amount-in-months-of-{cardNumber}-in-{year}")
    public List<Double> getTotalTransactionAmountInMonths( @PathVariable("username") String username,
                                                           @PathVariable("cardNumber") String cardNumber,
                                                           @PathVariable("year") Integer year){
        return transactionService.getTotalTransactionAmountInMonths(username, cardNumber, year);
    }

    @GetMapping (path ="{username}/amount-in-days-of-{cardNumber}-in-{month}-{year}")
    public List<Pair<Integer,Double>> getTotalTransactionAmountInDays(@PathVariable("username") String username,
                                                                      @PathVariable("cardNumber") String cardNumber,
                                                                      @PathVariable("year") Integer year,
                                                                      @PathVariable("month") Integer month){
        return transactionService.getTotalTransactionAmountInDays(username, cardNumber,month, year);
    }


}
