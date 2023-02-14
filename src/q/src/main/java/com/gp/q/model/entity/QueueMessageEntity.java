package com.gp.q.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "queue")
@Data
@NoArgsConstructor
public class QueueMessageEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String message;
    private LocalDateTime creationDate;

    /**
     * @param name    Имя очереди
     * @param message Полезная нагрузка
     */
    public QueueMessageEntity(String name, String message) {
        this(name, message, LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
    }

    /**
     * @param name         Имя очереди
     * @param message      Полезная нагрузка
     * @param creationDate - Время создания
     */
    public QueueMessageEntity(String name, String message, LocalDateTime creationDate) {
        this.name = name;
        this.message = message;
        this.creationDate = creationDate;
    }
}
