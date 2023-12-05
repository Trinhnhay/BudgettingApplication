package com.example.demo.Loan;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name="loan")
/*
    loan_id, username, loan_amount, interest_rate, loan_status, loan_start_date, loan_due_date,
    remaining_balance, tardy_interest_rate, next_payment_due
 */
public class Loan {

    @Id
    @Column (name ="loan_id",
            nullable = false)
    private Integer loanId;

    @Column(name ="username",
            nullable = false,
            columnDefinition = "text")
    private String username;

    @Column(name ="loan_amount",
            nullable = false)
    private Double loanAmount;

    @Column(name ="interest_rate",
            nullable = false)
    private Double interestRate;

    @Column(name ="loan_status",
            nullable = false,
            columnDefinition = "VARCHAR(20)")
    private String loanStatus;

    @Column(name="loan_start_date",
            nullable = false
    )
    private Date loanStartDate;

    @Column(name="loan_due_date",
            nullable = false
    )
    private Date loanDueDate;

    @Column(name ="remaining_balance",
            nullable = false)
    private Double remainingBalance;

    @Column(name ="tardy_interest_rate",
            nullable = false)
    private Double tardyInterestRate;

    @Column (name="next_payment_due")
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date nextPaymentDueDate;

    @Column (name ="payment_due",
            columnDefinition = "double precision")
    private Double paymentDue;

    // Getters
    public Integer getLoanId() {
        return loanId;
    }

    public String getUsername() {
        return username;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public Date getLoanStartDate() {
        return loanStartDate;
    }

    public Date getLoanDueDate() {
        return loanDueDate;
    }

    public Double getRemainingBalance() {
        return remainingBalance;
    }

    public Double getTardyInterestRate() {
        return tardyInterestRate;
    }

    public Date getNextPaymentDueDate() {
        return nextPaymentDueDate;
    }

    public Double getPaymentDue() {
        return paymentDue;
    }
}

