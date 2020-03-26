package com.nexos.nexos_admin.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: nexos_admin
 * @description:
 * @author: afsun
 * @create: 2020-03-26 12:14
 */
@Api("登录")
@Data
public class SysUserLoginVO implements Serializable {

    @ApiModelProperty("用户名")
    private  String userName;

    @ApiModelProperty("密码")
    private String password;

}