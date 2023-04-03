package com.gp.q.repository;

import com.gp.q.model.entity.QueuePropertyEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Репозиторий хранящий информацию о созданных очередях
 */
public interface QueuePropertyRepository extends CrudRepository<QueuePropertyEntity, String> {

}
