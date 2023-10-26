package com.example.demo.CreditCard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "credit_card")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @Autowired
    public CreditCardController(CreditCardService creditCardService){
        this.creditCardService = creditCardService;
    }

    //Add a credit card
    @PostMapping(path= "{username}/new")
    public void addCreditCard (@PathVariable ("username") String username, @RequestBody CreditCard creditCard){
        creditCardService.addCreditCard (creditCard, username);
    }
}
