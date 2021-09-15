package com.jaanu.bankapp.resource;


import com.jaanu.bankapp.dto.TransactionRequestDto;
import com.jaanu.bankapp.dto.TransactionResponse;
import com.jaanu.bankapp.dto.Transactions;
import com.jaanu.bankapp.exception.BadRequestException;
import com.jaanu.bankapp.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequiredArgsConstructor
public class TransactionResource {

    private final TransactionService transactionService;

    @PostMapping(value = "/transfer/{cusId}")
    public ResponseEntity<TransactionResponse> transfer(@PathVariable("cusId") Integer cusId, @RequestParam("operation") String operation,@Valid @RequestBody TransactionRequestDto request) {
        if(operation.isEmpty()){
            throw new BadRequestException("Operation param cannot be empty");
        }
        request.setOperationType(operation);
        TransactionResponse response = transactionService.transfer(cusId, request);

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @GetMapping("/transactions/{cusId}")
    public  ResponseEntity<Transactions> getTransactions(@PathVariable("cusId") Integer cusId,@RequestParam("fromDate") @DateTimeFormat(pattern="yyyy-MM-dd")Date fromDate,
                                                         @RequestParam("toDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate){
        Transactions response = transactionService.getTransactions(cusId,fromDate,toDate);
        return ResponseEntity.ok(response);
    }



}
