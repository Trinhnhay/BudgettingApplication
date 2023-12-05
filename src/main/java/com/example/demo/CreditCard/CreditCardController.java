package com.example.demo.CreditCard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "credit_card")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @Autowired
    public CreditCardController(CreditCardService creditCardService){
        this.creditCardService = creditCardService;
    }

    //Add a credit card
    @PostMapping(path= "{username}/add_new_card")
    public void addCreditCard (@PathVariable ("username") String username,
                               @RequestBody CreditCard creditCard){
        creditCardService.addCreditCard (creditCard, username);
    }

    @GetMapping (path ="current-balance-of-{cardNumber}")
    public Double getCurrentBalance(@PathVariable ("cardNumber") String cardNumber){
        return creditCardService.getCurrentBalance(cardNumber);
    }

    @GetMapping(path="{username}")
    public List<CreditCard> getCardsByUsername(@PathVariable("username") String username) {
        return creditCardService.getCardsByUsername(username);
    }




}
