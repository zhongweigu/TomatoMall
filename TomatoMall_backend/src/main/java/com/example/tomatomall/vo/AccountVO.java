package com.example.tomatomall.vo;

import com.example.tomatomall.enums.RoleEnum;
import com.example.tomatomall.po.Account;
import com.example.tomatomall.po.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AccountVO {

    private Integer id;

    private String username;

    private String name;

    private String telephone;

    private String password;

    private Integer storeId;

    private String location;

    private RoleEnum role;

    private String avatar;

    private String email;

    private Set<OrderVO> orderVOs;

    public Account toPO(){
        Account user=new Account();
        user.setId(this.id);
        user.setLocation(this.location);
        user.setUsername(this.username);
        user.setName(this.name);
        user.setTelephone(this.telephone);
        user.setRole(this.role);
        user.setStoreId(this.storeId);
        user.setPassword(this.password);
        user.setAvatar(this.avatar);
        user.setEmail(this.email);
        return user;
    }
}
