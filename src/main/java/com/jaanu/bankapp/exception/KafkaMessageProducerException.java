package com.jaanu.bankapp.exception;

public class KafkaMessageProducerException extends RuntimeException {
    public KafkaMessageProducerException(String message) {
        super(message);
    }
}
