package com.gp.q.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueuePropertyDto {

    /**
     * Имя очереди.
     * Не может начинаться с "amq." и быть больше 255 символов.
     * <br>
     * <a href="https://www.rabbitmq.com/queues.html#names">Queue names may be up to 255 bytes of UTF-8 characters </a>
     */
    @Pattern(regexp = "^(?!amq[.]).*$")
    @Size(min = 1, max = 255)
    @JsonProperty("queue_name")
    @JsonAlias("queueName")
    private String queueName;


    /**
     * Имя создателя очереди.
     */
    @JsonIgnore
    private String creator;
}