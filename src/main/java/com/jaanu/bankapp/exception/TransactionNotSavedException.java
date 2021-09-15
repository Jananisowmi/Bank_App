package com.jaanu.bankapp.exception;

public class TransactionNotSavedException extends RuntimeException {
    public TransactionNotSavedException(String message) {
        super(message);
    }
}
