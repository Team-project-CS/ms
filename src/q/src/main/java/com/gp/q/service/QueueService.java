package com.gp.q.service;

import com.gp.q.model.dto.QueueLogDto;
import com.gp.q.model.dto.QueueLogPeriodDto;
import com.gp.q.model.dto.QueueMessageDto;

import java.time.LocalDateTime;
import java.util.List;

public interface QueueService {
    /**
     * Добавляет сообщение в указанную очередь
     *
     * @return Добавленное сообщение
     */
    QueueMessageDto pushInQueue(QueueMessageDto queueMessageDto);

    /**
     * Извлекает сообщение из указанной очереди
     *
     * @param queueName Имя очереди
     * @return Извлеченное сообщение
     */
    QueueMessageDto popFromQueue(String queueName);

    /**
     * Возвращает все сообщение когда-либо поступавшие в указанную очередь
     *
     * @param queueName Имя очереди
     * @return Все сообщение когда-либо поступавшие в указанную очередь
     */
    List<QueueLogDto> getAllMessages(String queueName);

    /**
     * Возвращает все сообщение когда-либо поступавшие в очереди за указанный период
     *
     * @param begin Начало периода
     * @param end   Конец периода
     * @return Все сообщение когда-либо поступившие в указанную очередь
     */
    List<QueueLogDto> getAllMessages(LocalDateTime begin, LocalDateTime end);

    /**
     * Возвращает все сообщение когда-либо поступавшие в очередь за указанный период
     *
     * @return Все сообщение когда-либо поступившие в указанную очередь за период
     */
    List<QueueLogDto> getAllMessages(QueueLogPeriodDto dto);
}
