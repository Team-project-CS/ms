package com.gp.q.controller;

import com.gp.q.service.QueueService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class QueueControllerValidationTest {
    @MockBean
    private QueueService service;

    @Autowired
    private MockMvc mockMvc;

    private static final String correctJsonFormat = """
            {
              "name": "queue1",
              "message": "{/.....}"
            }""";

    private static final String wrongJsonFormatEmptyName = """
            {
              "name": ""
            }""";

    private static final String wrongJsonFormatNoName = """
            {
            }""";


    private static final String wrongJsonFormatReservedName = """
            {
              "name": "amq.aaaa",
              "message": "{/.....}"
            }""";

    @Test
    void createTaskJson() throws Exception {
        mockMvc.perform(post("/queue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(correctJsonFormat)
                )
                .andExpect(status().isOk());
    }

    @Test
    void createTaskWrongJsonEmptyName() throws Exception {
        mockMvc.perform(post("/queue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(wrongJsonFormatEmptyName)
                )
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void createTaskWrongJsonNoName() throws Exception {
        mockMvc.perform(post("/queue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(wrongJsonFormatNoName)
                )
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void createTaskWrongJsonReservedName() throws Exception {
        mockMvc.perform(post("/queue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(wrongJsonFormatReservedName)
                )
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
}