package com.gp.q.controller;

import com.gp.q.service.QueueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class QueueHistoryControllerValidationTest {

    public static final String PATH = "/queue/history";
    @MockBean
    private QueueService service;

    @Autowired
    private MockMvc mockMvc;

    private static final String getAllMessagesByPeriodAndName = """
            {
              "name": "queue1",
              "start": "2023-02-13T14:55:18.497007",
              "end": "2023-02-13T14:55:38.497007"
            }""";


    @Test
    void getMessagesByQueueName() throws Exception {
        mockMvc.perform(get(PATH + "/queue1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getMessagesByDate() throws Exception {
        mockMvc.perform(get(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(getAllMessagesByPeriodAndName)
                )
                .andExpect(status().is2xxSuccessful());
    }
}