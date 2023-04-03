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
@Table(name = "endoint", schema = "mock")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EndpointEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @NotNull
    @Column(name = "title")
    private String title;
    @OneToMany(mappedBy = "bodyEndpointEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ParamEntity> bodyTemplate = new HashSet<>();
    @OneToMany(mappedBy = "responseEndpointEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ParamEntity> responseTemplate = new HashSet<>();
    @Column(name = "description")
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private Method method;
    @Column(name = "proceed_logic")
    private String proceedLogic;

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
