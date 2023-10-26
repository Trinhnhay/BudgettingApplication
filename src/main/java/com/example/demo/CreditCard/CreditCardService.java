package com.example.demo.CreditCard;
import com.example.demo.Exception.RequestException;
import com.example.demo.Transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardService {
    private final CreditCardRepository creditCardRepository;
    private final TransactionService transactionService;
    @Autowired
    public CreditCardService (CreditCardRepository creditCardRepository, TransactionService transactionService ){
        this.creditCardRepository = creditCardRepository;
        this.transactionService = transactionService;
    }

    public void addCreditCard (CreditCard creditCard, String username) {
        if (transactionService.isUsernamePresent(username)) {
            creditCard.setUsername(username);
            creditCardRepository.save(creditCard);
        }
        else
            throw new RequestException("This username does not exist");
    }
}
