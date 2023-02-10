package com.gp.q.config;

import com.gp.q.component.QueueMessagePropertyMap;
import com.gp.q.component.QueuePropertyMap;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new QueueMessagePropertyMap());
        modelMapper.addMappings(new QueuePropertyMap());
        return modelMapper;
    }
}
