package com.gp.api.model.pojo;

import com.gp.api.model.types.ParamType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Param {
    private String key;
    private String value;
    private ParamType type;
}
