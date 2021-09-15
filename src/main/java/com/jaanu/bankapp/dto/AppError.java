package com.jaanu.bankapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppError {

    private String status;
    private String message;
    private LocalDateTime timeStamp;
    private String path;


}
