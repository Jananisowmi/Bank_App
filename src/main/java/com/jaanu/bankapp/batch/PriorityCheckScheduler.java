package com.jaanu.bankapp.batch;

import com.jaanu.bankapp.producer.PriorityCustomerProducer;
import com.jaanu.bankapp.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "scheduler.enabled",
        havingValue = "true"
)
public class PriorityCheckScheduler {

    private final AccountRepository accountRepository;

    private final PriorityCustomerProducer producer;

    @Scheduled(fixedDelayString = "${scheduler.delay}", initialDelayString = "3000")
    public void prioritize() {

        log.info("Scheduler running");

        addPriorityCustomers();
        removePriorityCustomers();

    }

    private void removePriorityCustomers() {
        final List<Integer> custlist = accountRepository.findAccountsByBalanceLimitLow();
        producer.removeCustomer(custlist);

    }

    private void addPriorityCustomers() {

        final List<Integer> custList = accountRepository.findAccountsByBalanceLimit();

        producer.addCustomer(custList);

        log.info("Scheduler process finished successfully");
    }
}
