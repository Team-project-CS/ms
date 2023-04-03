package com.gp.api.repository;

import com.gp.api.model.entity.EndpointLogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EndpointLogRepository extends CrudRepository<EndpointLogEntity, UUID> {
    List<EndpointLogEntity> findByEndpointIdAndCreationDateBetween(UUID endpointId, LocalDateTime creationDateStart, LocalDateTime creationDateEnd);

    List<EndpointLogEntity> findByEndpointIdAndCreationDateGreaterThanEqual(UUID endpointId, LocalDateTime creationDate);

    List<EndpointLogEntity> findByEndpointId(UUID endpointId);

    List<EndpointLogEntity> findByEndpointIdAndCreationDateLessThanEqual(UUID endpointId, LocalDateTime creationDate);

}
