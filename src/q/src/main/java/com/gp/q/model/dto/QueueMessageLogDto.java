package com.gp.q.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gp.q.model.QueueMessageDirection;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;


@Data
@NoArgsConstructor
public class QueueMessageLogDto {

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @ApiModelProperty(
            example = "14-03-2023 03:11:26",
            value = "Date and time at which the message was pushed or popped from the queue")
    private LocalDateTime creationDate  = LocalDateTime.now();

    public QueueMessageLogDto(String name, @NotNull String message, QueueMessageDirection direction) {
        this(name, message, direction, LocalDateTime.now());
    }

    public QueueMessageLogDto(String name, @NotNull String message, QueueMessageDirection direction, LocalDateTime creationDate) {
        this.name = name;
        this.message = message;
        this.direction = direction;
        this.creationDate = Objects.requireNonNullElseGet(creationDate, LocalDateTime::now);
    }
}