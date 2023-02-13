package com.gp.q.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "queue")
@Data
@NoArgsConstructor
public class QueueMessageEntity {
    @Id
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
