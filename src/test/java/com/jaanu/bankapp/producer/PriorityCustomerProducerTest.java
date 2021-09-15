package com.jaanu.bankapp.producer;


import com.jaanu.bankapp.exception.KafkaMessageProducerException;
import com.jaanu.bankapp.model.Account;
import com.jaanu.bankapp.model.Customer;
import com.jaanu.bankapp.model.PriorityCustomer;
import com.jaanu.bankapp.repository.CustomerRepository;
import com.jaanu.bankapp.repository.PriorityCustomerRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class PriorityCustomerProducerTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PriorityCustomerRepository prCustomerRepository;

    @InjectMocks
    private PriorityCustomerProducer producer;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ListenableFuture<SendResult<String, String>> future;

    @Mock
    private SendResult<String, String> sendResult;

    @Mock
    private Throwable throwable;


    @Test
    void shouldAddCustomerToPriorityTable() {

        Account account = Account.builder().accountId(1).accountNumber(UUID.randomUUID().toString())
                .balance(100000D)
                .build();
        Optional<Customer> customer = Optional.of(new Customer(1, "jaanu", "jaanu@gmail.com", "MTP", account));

        when(customerRepository.findCustomerByCusId(anyInt())).thenReturn(customer);
        when(prCustomerRepository.findByCusId(anyInt())).thenReturn(Optional.empty());

        PriorityCustomer priorityCustomer = new PriorityCustomer(1, 1, "Jaanu");

        when(prCustomerRepository.save(any(PriorityCustomer.class))).thenReturn(priorityCustomer);

        List<Integer> custList = new ArrayList<>();
        custList.add(1);


        List<String> list = new ArrayList<>();
        list.add("Jaanu");


        final ProducerRecord record = new ProducerRecord("Priority_Customers", list.toString());

        when(sendResult.getProducerRecord()).thenReturn(record);
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(future);
        doAnswer(invocationOnMock -> {
            ListenableFutureCallback listenableFutureCallback = invocationOnMock.getArgument(0);
            listenableFutureCallback.onSuccess(sendResult);
            assertEquals(list.toString(), sendResult.getProducerRecord().value());
            return future;
        }).when(future).addCallback(any(ListenableFutureCallback.class));

        producer.addCustomer(custList);

        verify(kafkaTemplate, times(1)).send("Priority_Customers", list.toString());

    }

    @Test
    void shouldThrowException() {
        Account account = Account.builder().accountId(1).accountNumber(UUID.randomUUID().toString())
                .balance(100000D)
                .build();
        Optional<Customer> customer = Optional.of(new Customer(1, "jaanu", "jaanu@gmail.com", "MTP", account));

        when(customerRepository.findCustomerByCusId(anyInt())).thenReturn(customer);
        when(prCustomerRepository.findByCusId(anyInt())).thenReturn(Optional.empty());

        PriorityCustomer priorityCustomer = new PriorityCustomer(1, 1, "Jaanu");

        when(prCustomerRepository.save(any(PriorityCustomer.class))).thenReturn(priorityCustomer);

        List<Integer> custList = new ArrayList<>();
        custList.add(1);


        List<String> list = new ArrayList<>();
        list.add("Jaanu");


        final ProducerRecord record = new ProducerRecord("Priority_Customers", list.toString());

        when(sendResult.getProducerRecord()).thenReturn(record);
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(future);
        doAnswer(invocationOnMock -> {
            ListenableFutureCallback listenableFutureCallback = invocationOnMock.getArgument(0);
            listenableFutureCallback.onFailure(throwable);
            return null;
        }).when(future).addCallback(any(ListenableFutureCallback.class));

        assertThrows(KafkaMessageProducerException.class, () -> producer.addCustomer(custList));


    }


    @Test
    void shouldRemoveCustomer() {

        doNothing().when(prCustomerRepository).deleteByCusId(1);

        List<Integer> custList = new ArrayList<>();
        custList.add(1);

        producer.removeCustomer(custList);

        verify(prCustomerRepository, atLeastOnce()).deleteByCusId(1);

    }
}
