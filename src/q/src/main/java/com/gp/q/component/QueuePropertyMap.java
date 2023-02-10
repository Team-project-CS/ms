package com.gp.q.component;

import com.gp.q.model.dto.QueuePropertyDto;
import com.gp.q.model.entity.QueueMessageEntity;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;

public class QueuePropertyMap extends PropertyMap<QueuePropertyDto, QueueMessageEntity> {
    @Override
    protected void configure() {
        using(mapping());
    }

    private Converter<QueuePropertyDto, QueueMessageEntity> mapping() {
        return context -> {
            QueuePropertyDto dto = context.getSource();
            return new QueueMessageEntity(dto.getQueueName(), dto.getCreator());
        };
    }
}
