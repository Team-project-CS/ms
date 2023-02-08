package com.gp.q.component;

import com.gp.q.model.dto.QueueMessageDto;
import com.gp.q.model.entity.QueueMessageEntity;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;

public class QueueMessagePropertyMap extends PropertyMap<QueueMessageDto, QueueMessageEntity> {
    @Override
    protected void configure() {
        using(generateFullname());
    }

    private Converter<QueueMessageDto, QueueMessageEntity> generateFullname() {
        return context -> {
            QueueMessageDto dto = context.getSource();
            return new QueueMessageEntity(dto.getName(), dto.getMessage());
        };
    }
}
