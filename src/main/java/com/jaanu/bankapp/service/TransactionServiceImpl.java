package com.jaanu.bankapp.service;

import com.jaanu.bankapp.dto.TransactionRequestDto;
import com.jaanu.bankapp.dto.TransactionResponse;
import com.jaanu.bankapp.dto.Transactions;
import com.jaanu.bankapp.exception.AccountNotFoundException;
import com.jaanu.bankapp.exception.MinRequiredBalanceException;
import com.jaanu.bankapp.exception.TransactionNotSavedException;
import com.jaanu.bankapp.model.Account;
import com.jaanu.bankapp.model.Transaction;
import com.jaanu.bankapp.repository.AccountRepository;
import com.jaanu.bankapp.repository.TransactionRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private static final String DEPOSIT = "deposit";
    private final AccountRepository accountRepository;
    private final TransactionRespository transactionRespository;

    @Transactional
    @Override
    public TransactionResponse transfer(Integer cusId, TransactionRequestDto request) {

        log.info("Deposit of amount started");
        TransactionResponse response = new TransactionResponse();
        Optional<Account> optionalAccount = accountRepository.findAccountByAccountNumber(request.getAccountNumber());
        if (!optionalAccount.isPresent()) {
            throw new AccountNotFoundException("Account not found for the account number");
        }
        Double amount = calcAmount(optionalAccount.get().getBalance(), request.getAmount(), request.getOperationType());
        accountRepository.updateAccount(amount, optionalAccount.get().getAccountNumber());
        log.info("Amount updated successfully");
        Transaction transaction = Transaction.builder().accountNumber(optionalAccount.get().getAccountNumber())
                .amount(request.getAmount())
                .transactionId(UUID.randomUUID()).account(optionalAccount.get()).timeStamp(LocalDate.now())
                .type(request.getOperationType()).build();
        Transaction savedTransaction = transactionRespository.save(transaction);

        if (Optional.ofNullable(savedTransaction.getTransactionId()).isPresent() ) {
            log.info("The transaction completed successfully {}", savedTransaction.getTransactionId());
            response.setTransactionId(savedTransaction.getTransactionId().toString());
            response.setMessage("The amount " + request.getOperationType() + " completed successfully");
        }
        else{
            throw new TransactionNotSavedException("The performed transaction is not saved successfully");
        }
        return response;
    }

    @Override
    public Transactions getTransactions(Integer cusId, Date fromDate, Date toDate) {
        log.info("Fetching transaction list for customer ID {}",cusId);
        List<Transaction> transactionList = new ArrayList<>();
        Optional<Account> account = accountRepository.findAccountByCusId(cusId);
        if(account.isPresent()) {
            transactionList = transactionRespository.findAllByDateRange(account.get().getAccountId(), fromDate, toDate);
        }
        return new Transactions(transactionList);
    }

    private Double calcAmount(Double balance, Double requestAmount, String operationType) {
        Double calcAmount = null;
        if (operationType.equalsIgnoreCase(DEPOSIT)) {
            calcAmount = balance + requestAmount;

        } else {
            calcAmount = balance - requestAmount;
            if (calcAmount <= 500) {
                throw new MinRequiredBalanceException("Minimum balance should be 500");
            }
        }
        return calcAmount;
    }
}
