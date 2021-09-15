package com.jaanu.bankapp;

import com.jaanu.bankapp.model.Account;
import com.jaanu.bankapp.model.Customer;
import com.jaanu.bankapp.repository.AccountRepository;
import com.jaanu.bankapp.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.UUID;

@SpringBootApplication
@EnableScheduling
public class BankappApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankappApplication.class, args);
    }

    @Bean
    CommandLineRunner initialize(CustomerRepository repository, AccountRepository accountRepository) {
        return args -> {

            Account account = Account.builder().accountId(1).accountNumber(UUID.randomUUID().toString())
                    .balance(100000D)
                    .build();
            Customer customer = Customer.builder().cusId(1).
                    customerName("Jaanu").
                    address("Mtp").
                    email("Jaanu@gmil.com")
                    .build();

			account.setCustomer(customer);
            customer.setAccount(account);

            repository.save(customer);

        };
    }

}
