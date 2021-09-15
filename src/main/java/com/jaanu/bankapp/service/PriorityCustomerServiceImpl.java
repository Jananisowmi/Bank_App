package com.jaanu.bankapp.service;

import com.jaanu.bankapp.dto.PriorityCustomerResponse;
import com.jaanu.bankapp.model.PriorityCustomer;
import com.jaanu.bankapp.repository.PriorityCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriorityCustomerServiceImpl implements  PriorityCustomerService{

    private final PriorityCustomerRepository priorityCustomerRepository;

    @Override
    public PriorityCustomerResponse getCustomers() {
        log.info("fetching priority Customers");
        List<PriorityCustomer> customerList = priorityCustomerRepository.findAll();
        return new PriorityCustomerResponse(customerList);
    }
}
