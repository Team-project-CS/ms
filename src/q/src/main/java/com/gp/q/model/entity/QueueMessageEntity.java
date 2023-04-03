package com.gp.q.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Data
@NoArgsConstructor
public class QueueMessageEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String message;

    /**
     * @param name    Имя очереди
     * @param message Полезная нагрузка
     */
    public QueueMessageEntity(String name, String message) {
        this.name = name;
        this.message = message;
    }
}
