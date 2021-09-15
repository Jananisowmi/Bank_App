package com.jaanu.bankapp.repository;

import com.jaanu.bankapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRespository extends JpaRepository<Transaction, Integer> {

    @Query(value = "SELECT * FROM TRANSACTION WHERE ACCOUNT_ID = ?1  AND TIME_STAMP BETWEEN ?2 AND ?3 ", nativeQuery = true)
    List<Transaction> findAllByDateRange(Integer cusId, Date fromDate, Date toDate);
}
