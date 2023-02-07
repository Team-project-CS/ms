package com.gp.api.model.entity;

import com.gp.api.model.ParamType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "endoint")
@Data
@NoArgsConstructor
public class EndpointEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    @ElementCollection
    private Map<String, ParamType> bodyTemplate;
    @ElementCollection
    private Map<String, ParamType> responseTemplate;
    private String description;
}
