package com.itinfo.itcloud.model.error;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Head {
    @ApiModelProperty(example = "상태코드")
    private int code;
    @ApiModelProperty(example = "메시지")
    private String message;
}
