package com.gp;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class QueueInfo {

    @NotBlank(message = "Name required")
    private String queueName;
}
