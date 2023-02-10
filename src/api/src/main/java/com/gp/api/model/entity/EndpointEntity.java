package com.gp.api.model.entity;

import com.gp.api.model.Param;
import com.gp.api.model.types.BodyParamType;
import com.gp.api.model.types.ResponseParamType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "endoint")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EndpointEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Param<BodyParamType>> bodyTemplate;
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Param<ResponseParamType>> responseTemplate;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EndpointEntity that = (EndpointEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
