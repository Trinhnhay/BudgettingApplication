package com.example.demo.Loan;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "loan")

public class LoanController {
    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService){this.loanService = loanService;}

    @GetMapping (path="remaining-balance-of-{loanId}")
    public Double getLoanRemainingBalance(@PathVariable("loanId") Integer loanId){
        return loanService.getLoanRemainingBalance(loanId);
    }

    @GetMapping (path="next-payment-due-amount-for-{loanId}")
    public Double getNextPaymentAmountDue(@PathVariable("loanId") Integer loanId){
        return loanService.getPaymentAmountDue(loanId);
    }

    @GetMapping (path="next-payment-due-date-for-{loanId}")
    public Date getNextPaymentDueDate(@PathVariable("loanId") Integer loanId){
        return loanService.getNextPaymentDueDate(loanId);
    }

    @GetMapping (path="loan-start-date-for-{loanId}")
    public Date getLoanStartDate(@PathVariable("loanId") Integer loanId){
        return loanService.getLoanStartDate(loanId);
    }
    @GetMapping (path="loan-due-date-{loanId}")
    public Date getLoanDueDate(@PathVariable("loanId") Integer loanId) {
        return loanService.getLoanDueDate(loanId);
    }

    @GetMapping (path="{username}")
    public List<Loan> getLoansByUsername(@PathVariable("username") String username){
        return loanService.getLoansByUsername(username);
    }



}
