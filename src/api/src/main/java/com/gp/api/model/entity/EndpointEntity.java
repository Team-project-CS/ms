package com.gp.api.model.entity;

import com.gp.api.model.types.ParamType;
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
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, ParamType> bodyTemplate;
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, ParamType> responseTemplate;
    private String description;
}
