package com.gp.q.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueueMessageDto {

    /**
     * Имя очереди
     */
    private @NotBlank String name;
    /**
     * Полезная нагрузка
     */
    private @NotNull String message;
}