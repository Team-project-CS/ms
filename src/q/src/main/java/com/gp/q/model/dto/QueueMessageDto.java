package com.gp.q.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gp.q.controller.QueueController;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Используется {@link QueueController} для вставки в очередь или для чтения из нее.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueueMessageDto {

    /**
     * Имя очереди.
     * Не может начинаться с "amq." и быть больше 255 символов.
     * <br>
     * <a href="https://www.rabbitmq.com/queues.html#names">Queue names may be up to 255 bytes of UTF-8 characters </a>
     */
    @ApiModelProperty(
            example = "queue1",
            value = "Queue name. Must not start with 'amq.' and be greater than 255 characters. " +
                    "Available aliases: {'name', 'queue', 'queue_name'}")
    @Pattern(regexp = "^(?!amq[.]).*$")
    @Size(min = 1, max = 255)
    @JsonProperty("name")
    @JsonAlias({"queue_name", "queue"})
    private String name;
    /**
     * Полезная нагрузка
     */
    @ApiModelProperty(
            example = "*****",
            value = "Message written to or read from the queue. Available aliases: {'message', 'payload'}")
    @NotNull
    @JsonProperty("message")
    @JsonAlias("payload")
    private String message;
}