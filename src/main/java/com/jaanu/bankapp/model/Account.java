package com.jaanu.bankapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;
    private String accountNumber;
    private Double balance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="cus_id")
    private Customer customer;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "account")
    @JsonManagedReference
    private List<Transaction> transactions;
}
