package com.example.tomatomall.vo;

import com.example.tomatomall.enums.RoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SimpleAccountVO {
    private String username;

    private String name;

    private String telephone;

    private Integer storeId;

    private String location;

    private RoleEnum role;

    private String avatar;

    private String email;
}
