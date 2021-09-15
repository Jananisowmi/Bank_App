package com.jaanu.bankapp.repository;

import com.jaanu.bankapp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    @Query(value = "SELECT * FROM CUSTOMER  WHERE CUS_ID = ?1", nativeQuery = true)
    Optional<Customer> findCustomerByCusId(Integer cusId);



}
