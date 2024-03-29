package com.gp.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gp.api.model.types.ParamType;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "param", schema = "mock")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParamEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "key")
    private String key;
    @Column(name = "value")
    private String value;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ParamType type;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_endpoint_id")
    @ToString.Exclude
    private EndpointEntity bodyEndpointEntity;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_endpoint_id")
    @ToString.Exclude
    private EndpointEntity responseEndpointEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ParamEntity that = (ParamEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
