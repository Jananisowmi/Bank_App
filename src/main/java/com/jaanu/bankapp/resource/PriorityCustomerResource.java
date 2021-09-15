package com.jaanu.bankapp.resource;

import com.jaanu.bankapp.dto.PriorityCustomerResponse;
import com.jaanu.bankapp.service.PriorityCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PriorityCustomerResource {

   private final PriorityCustomerService service;

    @GetMapping("/customers")
    public ResponseEntity<PriorityCustomerResponse> getCustomers(){
        return ResponseEntity.ok(service.getCustomers());
    }
}
