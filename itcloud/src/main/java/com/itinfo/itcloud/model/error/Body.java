package com.itinfo.itcloud.model.error;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Body<T> {
    @ApiModelProperty(example = "응답 데이터")
    private T content;
}