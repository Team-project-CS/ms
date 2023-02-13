package com.gp.api.repository;

import com.gp.api.model.entity.ParamEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParamRepository extends CrudRepository<ParamEntity, UUID> {
}
