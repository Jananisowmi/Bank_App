package com.jaanu.bankapp.repository;

import com.jaanu.bankapp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findAccountByAccountNumber(String accountNumber);

    @Modifying
    @Query(value = "UPDATE ACCOUNT SET  BALANCE = ? where ACCOUNT_NUMBER = ?", nativeQuery = true)
    Integer updateAccount(Double amount, String accountNumber);

    @Query(value = "SELECT CUS_ID FROM ACCOUNT WHERE BALANCE >= 10000 ", nativeQuery = true)
    List<Integer> findAccountsByBalanceLimit();

    @Query(value = "SELECT CUS_ID FROM ACCOUNT WHERE BALANCE < 10000 ", nativeQuery = true)
    List<Integer> findAccountsByBalanceLimitLow();

    @Query(value = "SELECT * FROM ACCOUNT WHERE CUS_ID = ?1",nativeQuery = true)
    Optional<Account> findAccountByCusId(Integer cusId);
}
