package com.jaanu.bankapp.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaanu.bankapp.dto.TransactionRequestDto;
import com.jaanu.bankapp.dto.TransactionResponse;
import com.jaanu.bankapp.dto.Transactions;
import com.jaanu.bankapp.exception.AccountNotFoundException;
import com.jaanu.bankapp.exception.MinRequiredBalanceException;
import com.jaanu.bankapp.exception.TransactionNotSavedException;
import com.jaanu.bankapp.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionResourceTest {

    private static final UUID TRANSACTION_ID = UUID.randomUUID();


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService service;


    private TransactionResource transactionResource;


    @BeforeEach
    void setUp(){
        transactionResource = new TransactionResource(service);
    }

    @Test
    void testDepositWithStatusCreated() throws Exception {
        TransactionResponse response = new TransactionResponse();
        response.setTransactionId(TRANSACTION_ID.toString());

        when(service.transfer(anyInt(), any(TransactionRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/transfer/{cusId}", 1)
                .param("operation", "deposit")
                .content(asJsonString(new TransactionRequestDto(1000D, UUID.randomUUID().toString(),"")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value(TRANSACTION_ID.toString()));


    }

    @Test
    void testWithdrawWithStatusCreated() throws Exception {
        TransactionResponse response = new TransactionResponse();
        response.setTransactionId(TRANSACTION_ID.toString());

        when(service.transfer(anyInt(), any(TransactionRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/transfer/{cusId}", 1)
                .param("operation", "withdraw")
                .content(asJsonString(new TransactionRequestDto(1000D, UUID.randomUUID().toString(),"")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value(TRANSACTION_ID.toString()));

    }

    @Test
    void shouldThrowException() throws Exception {

        mockMvc.perform(post("/transfer/{cusId}", 1)
                .param("operation", "")
                .content(asJsonString(new TransactionRequestDto(1000D, UUID.randomUUID().toString(),"")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

    }

    @Test
    void shouldThrowMinRequiredBalanceException() throws Exception {

        doThrow(MinRequiredBalanceException.class).when(service).transfer(anyInt(),any(TransactionRequestDto.class));
        mockMvc.perform(post("/transfer/{cusId}", 1)
                .param("operation", "withdraw")
                .content(asJsonString(new TransactionRequestDto(1000D, UUID.randomUUID().toString(),"")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

    }

    @Test
    void shouldThrowTransactionNotSavedException() throws Exception {

        doThrow(TransactionNotSavedException.class).when(service).transfer(anyInt(),any(TransactionRequestDto.class));
        mockMvc.perform(post("/transfer/{cusId}", 1)
                .param("operation", "withdraw")
                .content(asJsonString(new TransactionRequestDto(1000D, UUID.randomUUID().toString(),"")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());

    }

    @Test
    void shouldThrowAccountNotFoundException() throws Exception {

        doThrow(AccountNotFoundException.class).when(service).transfer(anyInt(),any(TransactionRequestDto.class));
        mockMvc.perform(post("/transfer/{cusId}", 2)
                .param("operation", "deposit")
                .content(asJsonString(new TransactionRequestDto(1000D, UUID.randomUUID().toString(),"")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());

    }


    @Test
    void shouldRetrieveRecordInDateRange() throws Exception {

       Transactions response = new Transactions();


        when(service.getTransactions(anyInt(),any(Date.class),any(Date.class))).thenReturn(response);

        mockMvc.perform(get("/transactions/{cusId}",1).param("fromDate","2021-09-14")
        .param("toDate", "2021-09-15").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
