package com.example.demo.Loan;
import com.example.demo.Customer.CustomerRepository;
import com.example.demo.Exception.RequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    private final CustomerRepository customerRepository;

    @Autowired
    public LoanService (LoanRepository loanRepository, CustomerRepository customerRepository){
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
    }

    public Loan doesLoanExist(Integer loanId){
        return loanRepository.findById(loanId)
                .orElseThrow(()-> new RequestException(
                        "This loan does not exist."
                ));
    }

    public Double getLoanRemainingBalance(Integer loanID){
        Loan loan = doesLoanExist(loanID);
        return loan.getRemainingBalance();
    }
    public Date getNextPaymentDueDate(Integer loanId){
        Loan loan = doesLoanExist(loanId);
        return loan.getNextPaymentDueDate();
    }

    public Double getPaymentAmountDue(Integer loanId){
        Loan loan  = doesLoanExist(loanId);
        return loan.getPaymentDue();
    }

    public Date getLoanStartDate(Integer loanId){
        Loan loan = doesLoanExist(loanId);
        return loan.getLoanStartDate();
    }

    public Date getLoanDueDate(Integer loanId){
        Loan loan = doesLoanExist(loanId);
        return loan.getLoanDueDate();
    }
}
