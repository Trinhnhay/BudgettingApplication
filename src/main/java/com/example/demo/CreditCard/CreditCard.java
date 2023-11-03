package com.example.demo.CreditCard;

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
@Table (name="credit_card")
public class CreditCard {

    @Id
    @Column (name ="card_number",
            nullable = false,
            columnDefinition = "text")
    private String cardNumber;

    @Column(name ="issuer",
            nullable = false,
            columnDefinition = "text")
    private String issuer;

    @Column(name ="interest_rate",
            nullable = false)
    private Double interestRate; // Assume it is monthly rate

    @Column(name ="credit_limit",
            nullable = false)
    private Double creditLimit;

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

    @Column (name ="username")
    private String username;

}
