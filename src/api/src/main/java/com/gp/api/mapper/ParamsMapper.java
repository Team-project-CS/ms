package com.gp.api.mapper;

import com.gp.api.model.dto.ParamDto;
import com.gp.api.model.entity.ParamEntity;

import java.util.Collection;

public interface ParamsMapper {

    Collection<ParamEntity> bodyParamsFromDto(Collection<ParamDto> bodyParams);

    Collection<ParamEntity> responseParamsFromDto(Collection<ParamDto> responseParams);

}
