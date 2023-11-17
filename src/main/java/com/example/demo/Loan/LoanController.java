package com.example.demo.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
