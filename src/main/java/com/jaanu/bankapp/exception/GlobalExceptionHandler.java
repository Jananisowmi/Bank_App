package com.jaanu.bankapp.exception;

import com.jaanu.bankapp.dto.AppError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<AppError> handleAccountNotFoundException(HttpServletRequest request, Exception ex) {
        log.info("Exception occurred while fetching account ");
        AppError error = new AppError(HttpStatus.NOT_FOUND.toString(), ex.getMessage(), LocalDateTime.now(), request.getServletPath());
        return  new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MinRequiredBalanceException.class)
    public ResponseEntity<AppError> handleMinRequiredBalanceException(HttpServletRequest request, Exception ex) {
        log.info("Minimum balance should be 500 ");
        AppError error = new AppError(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), LocalDateTime.now(), request.getServletPath());
        return  new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<AppError> handleBadRequestException(HttpServletRequest request, Exception ex) {
        log.info("Exception occurred");
        AppError error = new AppError(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), LocalDateTime.now(), request.getServletPath());
        return  new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionNotSavedException.class)
    public ResponseEntity<AppError> handleTransactionNotSavedException(HttpServletRequest request, Exception ex) {
        log.info("Exception occurred while saving transaction details");
        AppError error = new AppError(HttpStatus.NO_CONTENT.getReasonPhrase(), ex.getMessage(), LocalDateTime.now(), request.getServletPath());
        return  new ResponseEntity<>(error,HttpStatus.NO_CONTENT);
    }
}
