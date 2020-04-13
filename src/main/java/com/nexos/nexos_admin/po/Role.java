package com.nexos.nexos_admin.po;

import java.util.Date;
import lombok.Data;

@Data
public class Role {
    /**
     * 角色ID
     */
    private Long id;

    /**
     * 所属父级角色ID
     */
    private Long parentId;

    /**
     * 角色唯一CODE代码
     */
    private String code;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色介绍
     */
    private String intro;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改时间
     */
    private Date edited;

    /**
     * 修改人
     */
    private String editor;

    /**
     * 逻辑删除:0=未删除,1=已删除
     */
    private Boolean deleted;
}

