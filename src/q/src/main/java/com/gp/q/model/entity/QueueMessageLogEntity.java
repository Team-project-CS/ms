package com.gp.q.model.entity;

import com.gp.q.model.QueueMessageDirection;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "queue")
@Data
@NoArgsConstructor
public class QueueMessageLogEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String message;
    private LocalDateTime creationDate;
    private QueueMessageDirection direction;

    /**
     * @param name         Имя очереди
     * @param message      Полезная нагрузка
     * @param creationDate Время создания
     * @param direction    Указывает направления поступления сообщения в очередь
     */
    public QueueMessageLogEntity(String name, String message, LocalDateTime creationDate, QueueMessageDirection direction) {
        this.name = name;
        this.message = message;
        this.creationDate = creationDate;
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueueMessageLogEntity that = (QueueMessageLogEntity) o;
        return id.equals(that.id) && name.equals(that.name) && message.equals(that.message) && direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, message, direction);
    }
}
