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
public class Loan {

    @Id
    @Column (name ="loan_id",
            nullable = false)
    private Integer loan_id;

    @Column(name ="username",
            nullable = false,
            columnDefinition = "text")
    private String username;

    @Column(name ="interest_rate",
            nullable = false)
    private Double interestRate; // Assume it is monthly rate

    @Column(name ="loan_amount",
            nullable = false)
    private Double loan_amount;

    @Column(name ="current_balance",
            columnDefinition = "DOUBLE(10,2)")
    private Double currentBalance;

    @Column(name =" available_credit",
            columnDefinition = "DOUBLE(10,2)")
    private Double availableCredit;

    @Column (name ="minimum_payment",
            nullable = false)
    private Double minimumPayment;

    @Column (name="due_date")
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date dueDate;

    @Column (name ="payment_due",
            columnDefinition = "double precision")
    private Double paymentDue;

}

