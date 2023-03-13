package com.gp.q.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gp.q.model.QueueMessageDirection;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueueMessageLogDto {

    @ApiModelProperty(
            example = "queue1",
            value = "Queue name. Must not start with 'amq.' and be greater than 255 characters. " +
                    "Available aliases: {'name', 'queue', 'queue_name'}")
    @Size(min = 1, max = 255)
    @JsonProperty("name")
    private String name;

    @ApiModelProperty(
            example = "*****",
            value = "Message written to or read from the queue. Available aliases: {'message', 'payload'}")
    @NotNull
    @JsonProperty("message")
    private String message;

    @JsonProperty("direction")
    private QueueMessageDirection direction;
}