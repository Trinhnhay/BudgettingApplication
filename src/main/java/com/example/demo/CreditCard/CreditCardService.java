package com.example.demo.CreditCard;
import com.example.demo.Customer.CustomerRepository;
import com.example.demo.Exception.RequestException;
import com.example.demo.Transaction.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardService {
    private final CreditCardRepository creditCardRepository;
    private final CustomerRepository customerRepository;
    @Autowired
    public CreditCardService (CreditCardRepository creditCardRepository, CustomerRepository customerRepository){
        this.creditCardRepository = creditCardRepository;
        this.customerRepository= customerRepository;
    }

    public void addCreditCard (CreditCard creditCard, String username) {
        if (customerRepository.existsById(username)) {
            creditCard.setUsername(username);
            if (creditCard.getCurrentBalance()==null)
                creditCard.setCurrentBalance(0.0);
            creditCardRepository.save(creditCard);
        }
        else
            throw new RequestException("This username does not exist");
    }

    public CreditCard doesCreditCardExist(String cardNumber){
        return creditCardRepository.findById(cardNumber)
                .orElseThrow(()-> new RequestException(
                        "This credit card does not exist"
                ));
    }

    public Double getCurrentBalance(String cardNumber){
        CreditCard creditCard = doesCreditCardExist(cardNumber);
        return creditCard.getCurrentBalance();
    }

    @Transactional
    public void updateBalance (Transaction transaction){

        CreditCard  creditCard = doesCreditCardExist(transaction.getCardNumber());

        if (transaction.getTransCategory().equals("payment"))
            creditCard.setPaymentDue(transaction.getTransAmount());

        Double currentBalance = creditCard.getCurrentBalance() + transaction.getTransAmount();
        Double availableCredit = creditCard.getCreditLimit() - currentBalance ;
        creditCard.setAvailableCredit(availableCredit);

        creditCard.setCurrentBalance(currentBalance);
    }
}
