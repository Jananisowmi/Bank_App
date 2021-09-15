package com.jaanu.bankapp.producer;

import com.jaanu.bankapp.exception.KafkaMessageProducerException;
import com.jaanu.bankapp.model.Customer;
import com.jaanu.bankapp.model.PriorityCustomer;
import com.jaanu.bankapp.repository.CustomerRepository;
import com.jaanu.bankapp.repository.PriorityCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class PriorityCustomerProducer {

    private static final String TOPIC = "Priority_Customers";

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final CustomerRepository customerRepository;

    private final PriorityCustomerRepository prCustomerRepository;

    @Transactional
    public void addCustomer(List<Integer> cusList) {

        List<String> priorityCusList = new ArrayList<>();
        cusList.forEach(res -> {
            Optional<Customer> customer = customerRepository.findCustomerByCusId(res);
            Optional<PriorityCustomer> priorityCustomer = prCustomerRepository.findByCusId(customer.get().getCusId());
            if (!priorityCustomer.isPresent()) {
                log.info("the customer is {}", customer.get().getCustomerName());
                PriorityCustomer prCustomer = new PriorityCustomer();
                prCustomer.setCusId(customer.get().getCusId());
                prCustomer.setCustomerName(customer.get().getCustomerName());
                PriorityCustomer savedCustomer = prCustomerRepository.save(prCustomer);
                priorityCusList.add(savedCustomer.getCustomerName());
                log.info("the customer is {} finished", customer.get().getCustomerName());
            }
        });

        if (!priorityCusList.isEmpty()) {

            ListenableFuture<SendResult<String, String>> sendFutureCallBack = kafkaTemplate.send(TOPIC, priorityCusList.toString());
            sendFutureCallBack.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    throw  new KafkaMessageProducerException(throwable.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, String> sendResult) {

                    log.info("The current data is {}",sendResult.getProducerRecord().toString());
                }
            });

            log.info("Data published to kafka successfully");
        }

    }

    @Transactional
    public void removeCustomer(List<Integer> custlist) {
        custlist.forEach(res -> {
            prCustomerRepository.deleteByCusId(res);
            log.info("The record deleted success");
        });
    }

}
