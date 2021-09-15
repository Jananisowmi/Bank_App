package com.jaanu.bankapp.repository;

import com.jaanu.bankapp.model.PriorityCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriorityCustomerRepository extends JpaRepository<PriorityCustomer, Integer> {

    Optional<PriorityCustomer> findByCusId(Integer cusId);

    void deleteByCusId(Integer res);
}
