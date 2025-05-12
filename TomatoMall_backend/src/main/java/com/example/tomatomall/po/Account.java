package com.example.tomatomall.po;

import com.example.tomatomall.vo.AccountResponseVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.tomatomall.enums.RoleEnum;
import com.example.tomatomall.vo.AccountVO;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Account {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Length(max = 50)
    @Column(name = "username", nullable = false)
    private String username;

    @Basic
    @Length(max = 100)
    @Column(name = "password", nullable = false)
    private String password;

    @Basic
    @Length(max = 50)
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Basic
    @Length(max = 255)
    @Column(name = "avatar")
    private String avatar;

    @Basic
    @Pattern(regexp = "^1\\d{10}$")
    @Length(min = 11, max = 11)
    @Column(name = "telephone")
    private String telephone;

    @Basic
    @Email(message = "邮箱格式不正确")
    @Length(max = 100)
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "store_id")
    private Integer storeId;

    @Basic
    @Length(max = 255)
    @Column(name = "location")
    private String location;


    public AccountVO toVO(){
        AccountVO AccountVO=new AccountVO();
        AccountVO.setId(this.id);
        AccountVO.setName(this.name);
        AccountVO.setLocation(this.location);
        AccountVO.setUsername(this.username);
        AccountVO.setRole(this.role);
        AccountVO.setStoreId(this.storeId);
        AccountVO.setTelephone(this.telephone);
        AccountVO.setPassword(this.password);
        AccountVO.setAvatar(this.avatar);
        AccountVO.setEmail(this.email);

        return AccountVO;
    }

    public AccountResponseVO toSimpleVO(){
        AccountResponseVO vo = new AccountResponseVO();
        vo.setUsername(this.username);
        vo.setName(this.name);
        vo.setRole(this.role);
        vo.setAvatar(this.avatar);
        vo.setTelephone(this.telephone);
        vo.setEmail(this.email);
        vo.setLocation(this.location);
        vo.setId(this.id);
        return vo;
    }

}
