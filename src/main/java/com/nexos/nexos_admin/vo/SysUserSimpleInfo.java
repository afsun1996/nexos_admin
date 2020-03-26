package com.nexos.nexos_admin.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.nexos.nexos_admin.enums.SysUserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "com.nexos.nexos_admin.po.SysUser")
@Data
public class SysUserSimpleInfo implements Serializable {
    /**
     * 唯一id
     */
    @ApiModelProperty(value = "唯一id")
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;

    /**
     * 密码(加密过)
     */
    @ApiModelProperty(value = "密码(加密过)")
    private String pwd;

    /**
     * 电子邮箱
     */
    @ApiModelProperty(value = "电子邮箱")
    private String email;


    /**
     * 更新日期
     */
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private SysUserStatus deleted = SysUserStatus.ACCOUNT_VALID;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;

    private static final long serialVersionUID = 1L;
}

