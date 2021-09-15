package com.jaanu.bankapp.resource;

import com.jaanu.bankapp.service.PriorityCustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PriorityCustomerResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PriorityCustomerService priorityCustomerService;

    @Test
    void shouldRetreiveListofCustomers() throws Exception {



        when(priorityCustomerService.getCustomers()).thenReturn(null);

        mockMvc.perform(get("/customers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
