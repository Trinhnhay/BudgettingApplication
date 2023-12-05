package com.example.demo.Transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="transaction")
public class Transaction {

    @Id
    @Column (name ="trans_id", unique = true)
    @SequenceGenerator(name = "sequence", sequenceName = "transSequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long transID;


    @Column(name="card_number",
            nullable = false)
    private String cardNumber;

    @Column (name="merchant",
            columnDefinition ="text")
    private String merchant;

    @Column (name="trans_date",
            columnDefinition ="date")
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date transDate;

    @Column (name="trans_category",
            columnDefinition = "text",
            nullable = false)
    private String transCategory;

    @Column (name="trans_amount",
            columnDefinition = "DOUBLE(10,2)",
            nullable = false)
    private Double transAmount;

    @Column(name="username",
            columnDefinition = "text")
    private String username;

    public Transaction() {

    }
}
