package com.itinfo.itcloud.VO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UserVO {
    private int no;
    private String id;
    private String password;
    private String name;
    private String email;
    private boolean administrative;
    private int loginCount;
    private String block;
}
