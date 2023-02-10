package com.gp.q.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "queue_list")
@Data
@NoArgsConstructor
public class QueuePropertyEntity {

    /**
     * Имя очереди.
     * Не может начинаться с "amq." и быть больше 255 символов.
     * <br>
     * <a href="https://www.rabbitmq.com/queues.html#names">Queue names may be up to 255 bytes of UTF-8 characters </a>
     */
    @Id
    private String queueName;

    /**
     * Имя создателя очереди.
     */
    private String creator;
}