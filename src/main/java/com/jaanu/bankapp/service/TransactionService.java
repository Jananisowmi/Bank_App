package com.jaanu.bankapp.service;

import com.jaanu.bankapp.dto.TransactionResponse;
import com.jaanu.bankapp.dto.TransactionRequestDto;
import com.jaanu.bankapp.dto.Transactions;

import java.util.Date;

public interface TransactionService {
    TransactionResponse transfer(Integer cusId, TransactionRequestDto request);

    Transactions getTransactions(Integer cusId, Date fromDate, Date toDate);
}
