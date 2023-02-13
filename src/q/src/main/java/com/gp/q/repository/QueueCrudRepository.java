package com.gp.q.repository;

import com.gp.q.model.entity.QueueMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface QueueCrudRepository extends JpaRepository<QueueMessageEntity, UUID> {

    /**
     * Возвращает все сообщение когда-либо поступавшие в очередь
     *
     * @param name Имя очереди
     * @return Все сообщение когда-либо в указанную очередь
     */
    List<QueueMessageEntity> findAllByName(String name);

    /**
     * Возвращает все сообщение когда-либо поступавшие в очередь за указанный период
     *
     * @param startDate Начало периода
     * @param endDate   Конец периода
     * @return Все сообщение поступившие за указанный период
     */
    List<QueueMessageEntity> findByCreationDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Возвращает все сообщение когда-либо поступавшие в очередь за указанный период
     *
     * @param startDate Начало периода
     * @param endDate   Конец периода
     * @param name      Имя очереди
     * @return Все сообщение поступившие в указанную очередь за период
     */
    List<QueueMessageEntity> findByCreationDateBetweenAndName(LocalDateTime startDate, LocalDateTime endDate, String name);
}
