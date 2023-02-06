package com.gp.q.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "queue")
@Data
@NoArgsConstructor
public class QueueEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
}
