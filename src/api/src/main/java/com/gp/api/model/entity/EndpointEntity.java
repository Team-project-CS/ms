package com.gp.api.model.entity;

import com.gp.api.model.types.Method;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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
    @NotNull
    private String title;
    @OneToMany(mappedBy = "bodyEndpointEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ParamEntity> bodyTemplate = new HashSet<>();
    @OneToMany(mappedBy = "responseEndpointEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ParamEntity> responseTemplate = new HashSet<>();
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Method method;

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
