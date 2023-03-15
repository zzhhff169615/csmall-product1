package cn.tedu.csmall.product1.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AdminStandardVO implements Serializable {

    private Long id;

    private String username;

    private String nickname;

    private String avatar;

    private String phone;

    private String email;

    private String description;

    private Integer enable;

    private String lastLoginIp;

    private Integer loginCount;

    private LocalDateTime gmtLastLogin;

}