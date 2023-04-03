package com.gp.api.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "endpoint_log", schema = "mock")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EndpointLogEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @NotNull
    @Column(name = "endpoint_id")
    private UUID endpointId;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "endpoint_log_input_mapping",
            joinColumns = {@JoinColumn(name = "log_id", referencedColumnName = "id")},
            schema = "mock")
    @MapKeyColumn(name = "input_field_name")
    @Column(name = "input_field_value")
    private Map<String, String> input = new HashMap<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "endpoint_log_output_mapping",
            joinColumns = {@JoinColumn(name = "log_id", referencedColumnName = "id")},
            schema = "mock")
    @MapKeyColumn(name = "output_field_name")
    @Column(name = "output_field_value")
    private Map<String, String> output = new HashMap<>();
    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    public EndpointLogEntity(UUID endpointId, Map<String, String> input, Map<String, String> output) {
        this.endpointId = endpointId;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EndpointLogEntity that = (EndpointLogEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
