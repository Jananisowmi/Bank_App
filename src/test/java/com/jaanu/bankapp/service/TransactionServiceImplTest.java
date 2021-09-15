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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    private static final UUID TRANSACTION_ID = UUID.randomUUID();

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRespository transactionRespository;

    private TransactionServiceImpl transactionService;

    private Transaction transaction;

    private TransactionResponse response;

    private Account account;


    @BeforeEach
    void setup() {
        transaction = new Transaction();
        transaction.setTransactionId(TRANSACTION_ID);
        response = new TransactionResponse();
        response.setTransactionId(TRANSACTION_ID.toString());
        account = Account.builder().accountId(1).accountNumber(UUID.randomUUID().toString())
                .balance(100000D)
                .build();
        transactionService = new TransactionServiceImpl(accountRepository,transactionRespository);

    }

    @Test
    void testDepositSuccess() {

        Optional<Account> optionalAccount = Optional.of(account);

        //when
        doReturn(optionalAccount).when(accountRepository).findAccountByAccountNumber(anyString());
        doReturn(1).when(accountRepository).updateAccount(anyDouble(), anyString());
        doReturn(transaction).when(transactionRespository).save(any());

        TransactionRequestDto requestDto = buildRequest();
        requestDto.setOperationType("deposit");

        //then
        TransactionResponse deposit = transactionService.transfer(1, requestDto);


        assertEquals(deposit.getTransactionId(), response.getTransactionId());
    }

    @Test
    void testWithdrawSuccess() {

        Optional<Account> optionalAccount = Optional.of(account);

        //when
        doReturn(optionalAccount).when(accountRepository).findAccountByAccountNumber(anyString());
        doReturn(1).when(accountRepository).updateAccount(anyDouble(), anyString());
        doReturn(transaction).when(transactionRespository).save(any());

        TransactionRequestDto requestDto = buildRequest();

        //then
        TransactionResponse deposit = transactionService.transfer(1, requestDto);

        assertEquals(deposit.getTransactionId(), response.getTransactionId());
    }

    @Test
    void shouldThrowMinRequiredBalanceException() {

        account = Account.builder().accountId(1).accountNumber(UUID.randomUUID().toString())
                .balance(1000D)
                .build();
        Optional<Account> optionalAccount = Optional.of(account);
        //when
        doReturn(optionalAccount).when(accountRepository).findAccountByAccountNumber(anyString());

        TransactionRequestDto requestDto = buildRequest();

        //then
        assertThrows(MinRequiredBalanceException.class, () -> transactionService.transfer(1, requestDto));

    }

    @Test
    void shouldThrowAccountNotFoundException() {

        //when
        doReturn(Optional.empty()).when(accountRepository).findAccountByAccountNumber(anyString());

        TransactionRequestDto requestDto = buildRequest();

        //then
        assertThrows(AccountNotFoundException.class, () -> transactionService.transfer(1, requestDto));
    }

    @Test
    void shouldReturnListOfTransactions() throws ParseException {

        Transaction transaction = new Transaction();
        transaction.setTransactionId(TRANSACTION_ID);
        transaction.setAccountNumber(UUID.randomUUID().toString());
        transaction.setTimeStamp(LocalDate.now());
        transaction.setType("withdraw");
        transaction.setAmount(1000D);
        transaction.setAccount(account);

        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);

        when(accountRepository.findAccountByCusId(anyInt())).thenReturn(Optional.of(account));
        when(transactionRespository.findAllByDateRange(anyInt(),any(Date.class),any(Date.class))).thenReturn(transactionList);

        Date fromDate = formatter.parse("2021-09-14");
        Date toDate = formatter.parse("2021-09-15");

        Transactions response =transactionService.getTransactions(1,fromDate,toDate);

        assertEquals(response.getTransactionList().size(), transactionList.size());
    }

    @Test
    void shouldThrowTransactionNotSaveException(){
        Optional<Account> optionalAccount = Optional.of(account);

        //when
        doReturn(optionalAccount).when(accountRepository).findAccountByAccountNumber(anyString());
        doReturn(1).when(accountRepository).updateAccount(anyDouble(), anyString());
        doReturn(new Transaction()).when(transactionRespository).save(any());

        TransactionRequestDto requestDto = buildRequest();
        requestDto.setOperationType("deposit");

        //then
        assertThrows(TransactionNotSavedException.class,() -> transactionService.transfer(1, requestDto));

    }

    private TransactionRequestDto buildRequest() {
        TransactionRequestDto requestDto = new TransactionRequestDto();
        requestDto.setAccountNumber(UUID.randomUUID().toString());
        requestDto.setAmount(700D);
        requestDto.setOperationType("withdraw");
        return requestDto;
    }
}
