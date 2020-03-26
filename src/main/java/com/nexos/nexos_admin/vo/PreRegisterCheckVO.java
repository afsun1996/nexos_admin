package com.nexos.nexos_admin.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: nexos_admin
 * @description:
 * @author: afsun
 * @create: 2020-03-26 15:47
 */
@Api
@Data
public class PreRegisterCheckVO {

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("昵称")
    private String nickName;



}