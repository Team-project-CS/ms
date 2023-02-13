package com.gp.q.service;

import com.gp.q.model.dto.QueueMessageDto;

import java.time.LocalDateTime;
import java.util.List;

public interface QueueService {
    QueueMessageDto pushInQueue(QueueMessageDto queueMessageDto);

    QueueMessageDto popFromQueue(String id);

    /**
     * Возвращает все сообщение когда-либо поступавшие в указанную очередь
     *
     * @param queueName Имя очереди
     * @return Все сообщение когда-либо поступавшие в указанную очередь
     */
    List<QueueMessageDto> getAllMessages(String queueName);

    /**
     * Возвращает все сообщение когда-либо поступавшие в очереди за указанный период
     *
     * @param begin Начало периода
     * @param end   Конец периода
     * @return Все сообщение когда-либо поступившие в указанную очередь
     */
    List<QueueMessageDto> getAllMessages(LocalDateTime begin, LocalDateTime end);

    /**
     * Возвращает все сообщение когда-либо поступавшие в очередь за указанный период
     *
     * @param begin     Начало периода
     * @param end       Конец периода
     * @param queueName Имя очереди
     * @return Все сообщение когда-либо поступившие в указанную очередь
     */
    List<QueueMessageDto> getAllMessages(String queueName, LocalDateTime begin, LocalDateTime end);

}
