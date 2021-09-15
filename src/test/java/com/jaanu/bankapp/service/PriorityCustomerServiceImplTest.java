package com.jaanu.bankapp.service;

import com.jaanu.bankapp.dto.PriorityCustomerResponse;
import com.jaanu.bankapp.model.PriorityCustomer;
import com.jaanu.bankapp.repository.PriorityCustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PriorityCustomerServiceImplTest {

    @Mock
    private PriorityCustomerRepository priorityCustomerRepository;

    @InjectMocks
    private PriorityCustomerServiceImpl service;

    @Test
    void shouldReturnListOfPriorityCustomers(){

        PriorityCustomer priorityCustomer = new PriorityCustomer();
        priorityCustomer.setCustomerName("Jaanu");
        priorityCustomer.setCusId(1);
        priorityCustomer.setPcId(1);
        List<PriorityCustomer> priorityCustomers = new ArrayList<>();
        priorityCustomers.add(priorityCustomer);

       when(priorityCustomerRepository.findAll()).thenReturn(priorityCustomers);

        PriorityCustomerResponse customers = service.getCustomers();

        assertEquals(customers.getPriorityCustomers().size(),priorityCustomers.size());
    }

}
