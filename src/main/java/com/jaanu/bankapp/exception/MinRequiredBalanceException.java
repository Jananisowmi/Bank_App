package com.jaanu.bankapp.exception;

public class MinRequiredBalanceException extends RuntimeException {
    public MinRequiredBalanceException(String message) {
        super(message);
    }
}
