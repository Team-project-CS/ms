package com.gp.q.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gp.q.controller.QueueHistoryController;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Передается {@link QueueHistoryController} для указания интервала выборки логов
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueueLogPeriodDto {

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
    @Pattern(regexp = "^(?!amq[.]).*$")
    @Size(min = 1, max = 255)
    @JsonProperty("queue_name")
    @JsonAlias({"queueName", "name"})
    private String queueName;

    @ApiModelProperty(
            example = "2023-02-13T14:55:18.497007",
            value = "Start time. Available aliases: {'start_date', 'begin', 'begin_date', 'first', 'first_date'}")
    @JsonProperty("start")
    @JsonAlias({"start_date", "begin", "begin_date", "first", "first_date"})
    private LocalDateTime startDate;

    @ApiModelProperty(
            example = "2023-02-13T14:55:38.497007",
            value = "End time. Available aliases: {'end', 'end_date', 'last', 'last_date'}")
    @JsonProperty("end")
    @JsonAlias({"end_date", "last", "last_date"})
    private LocalDateTime endDate;

}
