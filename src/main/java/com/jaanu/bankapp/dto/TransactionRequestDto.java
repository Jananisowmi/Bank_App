package com.jaanu.bankapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {

    @Min(1)
    private Double amount;
    private String accountNumber;
    @JsonIgnore
    private String operationType;


}
