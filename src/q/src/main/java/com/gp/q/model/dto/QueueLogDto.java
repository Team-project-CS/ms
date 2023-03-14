package com.gp.q.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gp.q.controller.QueueHistoryController;
import com.gp.q.model.QueueMessageDirection;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.time.temporal.ChronoField.MINUTE_OF_DAY;

/**
 * Используется для GET запросов {@link QueueHistoryController}.
 * Содержит имя очереди, сообщение, совершаемых над очередью операций и их время выполнения.
 */
@Data
@NoArgsConstructor
public class QueueLogDto {

    @ApiModelProperty(
            example = "queue1",
            value = "Queue name. Must not start with 'amq.' and be greater than 255 characters")
    @Size(min = 1, max = 255)
    @JsonProperty("name")
    private String name;

    @ApiModelProperty(
            example = "*****",
            value = "Message written to or read from the queue")
    @NotNull
    @JsonProperty("message")
    private String message;

    @JsonProperty("direction")
    private QueueMessageDirection direction;

    @JsonProperty("creation")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @ApiModelProperty(
            example = "14-03-2023 03:11:26",
            value = "Date and time at which the message was pushed or popped from the queue")
    private LocalDateTime creationDate = LocalDateTime.now();

    public QueueLogDto(String name, @NotNull String message, QueueMessageDirection direction) {
        this(name, message, direction, LocalDateTime.now());
    }

    public QueueLogDto(String name, @NotNull String message, QueueMessageDirection direction, LocalDateTime creationDate) {
        this.name = name;
        this.message = message;
        this.direction = direction;
        this.creationDate = Objects.requireNonNullElseGet(creationDate, LocalDateTime::now);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueueLogDto that = (QueueLogDto) o;

        if (!name.equals(that.name)) return false;
        if (!message.equals(that.message)) return false;
        if (direction != that.direction) return false;
        if (!creationDate.toLocalDate().equals(that.creationDate.toLocalDate())) return false;
        return creationDate.getLong(MINUTE_OF_DAY) == that.creationDate.getLong(MINUTE_OF_DAY);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, message, direction, creationDate);
    }
}