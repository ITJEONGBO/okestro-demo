package com.itinfo.itcloud.model.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Body<T> {
    private T content;
}