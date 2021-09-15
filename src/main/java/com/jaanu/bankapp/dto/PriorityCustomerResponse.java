package com.jaanu.bankapp.dto;

import com.jaanu.bankapp.model.PriorityCustomer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriorityCustomerResponse {
    private List<PriorityCustomer>  priorityCustomers;
}
