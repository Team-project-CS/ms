package com.gp.q.repository;

import com.gp.q.model.entity.QueueEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QueueRepository extends CrudRepository<QueueEntity, UUID> {
}
