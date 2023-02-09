package com.gp.q.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


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
//    @NotBlank
    @Pattern(regexp = "^(?!amq[.]).*$")
//    @Max(255)
    @Size(min = 1, max = 255)
    private String name;
    /**
     * Полезная нагрузка
     */
    @NotNull
    private String message;
}