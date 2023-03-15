package cn.tedu.csmall.product1.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AdminLoginInfoVO implements Serializable {

    private Long id;

    private String username;

    private String password;

    private Integer enable;

    private List<String> permissions;

}