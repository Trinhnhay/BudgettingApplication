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
    @Column(name = "ID",
            nullable = false,
            columnDefinition = "Integer")
    @SequenceGenerator(name = "sequence", sequenceName = "sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Integer id;

    @Column (name ="card_number",
            nullable = false,
            columnDefinition = "text")
    private String cardNumber;

    @Column(name ="issuer",
            columnDefinition = "text")
    private String issuer;

    @Column(name ="interest_rate")
    private Double interestRate; // Assume it is monthly rate

    @Column(name ="credit_limit")
    private Double creditLimit;

    @Column(name ="current_balance")
    private Double currentBalance;

    @Column(name =" available_credit")
    private Double availableCredit;

    @Column (name ="minimum_payment")
    private Double minimumPayment;

    @Column (name="due_date")
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date dueDate;

    @Column (name ="payment_due")
    private Double paymentDue;

    @Column (name ="username")
    private String username;

}
